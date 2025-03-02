package org.example.meetlearning.service;

import com.aliyun.oss.common.utils.HttpHeaders;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.file.FilePageRespVo;
import org.example.meetlearning.vo.file.FileQueryVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.*;
import java.net.URLEncoder;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
public class FileService {


    @Value("${file.private-folder}")
    private String privateFolder;

    @Value("${file.public-folder}")
    private String publicFolder;

    @Value("${file.root-folder}")
    private String rootFolder;

    @Value("${https.net}")
    private String httpsNet;

    public List<FilePageRespVo> listFiles(FileQueryVo fileQueryVo) {
        List<FilePageRespVo> fileDetails = new ArrayList<>();
        Boolean isPrivate = fileQueryVo.getIsPrivate();
        String directoryPath = BooleanUtils.isTrue(isPrivate) ? privateFolder + "/" + fileQueryVo.getUserId() + "/" + fileQueryVo.getPath()
                : publicFolder + fileQueryVo.getPath();

        Path path = Paths.get(directoryPath);
        log.info("--->directoryPath:{}", directoryPath);

        // 如果目录不存在，则创建它
        if (!Files.exists(path) && BooleanUtils.isTrue(isPrivate)) {
            log.info("创建文件夹");
            try {
                Files.createDirectories(path); // 递归创建目录
                System.out.println("Directory created: " + directoryPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + directoryPath, e);
            }
        }
        if (!Files.exists(path)) {
            return fileDetails;
        }
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            for (Path entry : stream) {
                BasicFileAttributes attributes = Files.readAttributes(entry, BasicFileAttributes.class);
                LocalDateTime lastModifiedTime = LocalDateTime.ofInstant(attributes.lastModifiedTime().toInstant(), ZoneId.systemDefault());
                String formattedTime = lastModifiedTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Long size = attributes.isDirectory() ? null : attributes.size(); // 文件夹大小通常不适用
                String url = generateFileUrl(entry.toString());
                String type = attributes.isDirectory() ? "Folder" : "File";

                FilePageRespVo fileDetail = new FilePageRespVo();
                fileDetail.setLastModified(formattedTime);
                fileDetail.setName(entry.getFileName().toString());
                fileDetail.setPath(entry.toAbsolutePath().toString());
                fileDetail.setSize(size);
                fileDetail.setType(type);
                fileDetail.setUrl(url);
                fileDetails.add(fileDetail);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileDetails;
    }

    private String generateFileUrl(String filePath) {
        // 假设文件可以通过 HTTP 访问，这里可以根据实际情况生成 URL
        return httpsNet + filePath;
    }


    public ResponseEntity<StreamingResponseBody> getBinaryFile(String filePath) {

        Path rootPath = Paths.get(rootFolder); // 限制访问的根目录
        Path path = rootPath.resolve(filePath).normalize();

        if (!path.startsWith(rootPath)) {
            throw new IllegalArgumentException("Access denied: Invalid file path.");
        }

        // 检查文件是否存在
        if (!Files.exists(path) || !Files.isRegularFile(path)) {
            throw new RuntimeException("File not found or invalid path: " + filePath);
        }

        // 获取文件名
        String fileName = path.getFileName().toString();

        // 创建 StreamingResponseBody
        StreamingResponseBody stream = outputStream -> {
            try (InputStream inputStream = new BufferedInputStream(Files.newInputStream(path))) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (IOException e) {
                throw new RuntimeException("Failed to stream file", e);
            }
        };

        // 设置响应头
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(stream);
    }


    public RespVo<String> uploadFolder(String destinationPath, MultipartFile file) {
        if (file.isEmpty()) {
            return new RespVo<>("null", false, "The uploaded file cannot be empty");
        }
        // 指定上传文件的存储路径
        String filePath = destinationPath + file.getOriginalFilename();
        // 保存文件
        try {
            // 保存文件
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("File save failed", e);
            return new RespVo<>("File save failed", false, e.getMessage());
        }
        return new RespVo<>("File saved successfully");
    }

}
