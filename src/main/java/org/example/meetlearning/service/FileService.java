package org.example.meetlearning.service;

import com.aliyun.oss.common.utils.HttpHeaders;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.file.FilePageRespVo;
import org.example.meetlearning.vo.file.FileQueryVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
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
        String directoryPath = BooleanUtils.isTrue(isPrivate) ? privateFolder + fileQueryVo.getUserId() + "/" + fileQueryVo.getPath()
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
                if (StringUtils.isNotEmpty(fileQueryVo.getSearch())) {
                    if (!entry.getFileName().toString().contains(fileQueryVo.getSearch())) {
                        continue;
                    }
                }
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
        destinationPath = privateFolder + destinationPath;
        Path path = Paths.get(destinationPath);
        // 如果目录不存在，则创建它
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path); // 递归创建目录
                System.out.println("Directory created: " + destinationPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + destinationPath, e);
            }
        }
        // 指定上传文件的存储路径
        String filePath = destinationPath + file.getOriginalFilename();
        // 保存文件
        try {
            log.info("--->filePath:{}", filePath);
            // 保存文件
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("File save failed", e);
            return new RespVo<>("File save failed", false, e.getMessage());
        }
        return new RespVo<>("File saved successfully");
    }

    public RespVo<String> uploadPublicFolder(String destinationPath, MultipartFile file) {
        if (file.isEmpty()) {
            return new RespVo<>("null", false, "The uploaded file cannot be empty");
        }
        destinationPath = publicFolder + destinationPath;
        Path path = Paths.get(destinationPath);
        // 如果目录不存在，则创建它
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path); // 递归创建目录
                System.out.println("Directory created: " + destinationPath);
            } catch (IOException e) {
                throw new RuntimeException("Failed to create directory: " + destinationPath, e);
            }
        }
        // 指定上传文件的存储路径
        String filePath = destinationPath + file.getOriginalFilename();
        // 保存文件
        try {
            log.info("--->filePath:{}", filePath);
            // 保存文件
            file.transferTo(new File(filePath));
        } catch (IOException e) {
            log.error("File save failed", e);
            return new RespVo<>("File save failed", false, e.getMessage());
        }
        return new RespVo<>("File saved successfully");
    }

    public RespVo<String> createFolder(String destinationPath) {
        Assert.isTrue(StringUtils.isNotEmpty(destinationPath), "Path cannot be empty");
        destinationPath = publicFolder + destinationPath;
        Path path = Paths.get(destinationPath);
        // 如果目录不存在，则创建它
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path); // 递归创建目录
                System.out.println("Directory created: " + destinationPath);
            } catch (IOException e) {
                log.error("Failed to create directory: " + e.getMessage());
                throw new RuntimeException("Failed to create directory");
            }
        }
        return new RespVo<>("Folder created successfully");
    }

    public void deletedFolderOrFile(String destinationPath) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(destinationPath), "Path cannot be empty");
            Path path = Paths.get("usr/local/apps/material/public-folder/dist.zip");
            Assert.isTrue(!Files.exists(path), "The file or folder does not exist");
            // 删除文件或文件夹
            if (Files.isDirectory(path)) {
                // 删除文件夹（包括子文件和子目录）
                FileUtils.deleteDirectory(path.toFile()); // 使用Apache Commons IO
            } else {
                // 删除单个文件
                Files.delete(path);
            }
        } catch (Exception e) {
            log.error("Failed to delete file or folder: " + e);
            throw new RuntimeException("Failed to delete file or folder: " + destinationPath);
        }
    }

    public RespVo<String> removeDic(String destinationPath) {
        destinationPath = privateFolder + destinationPath;
        Path folder = Paths.get(destinationPath);

        // 检查文件夹是否存在
        if (!Files.exists(folder)) {
            log.info("---------->folder:{}", folder);
            return new RespVo<>("The folder does not exist", false, "The folder does not exist");
        }

        // 检查是否是文件夹
        if (!Files.isDirectory(folder)) {
            log.info("---------->folder:{}", folder);
            return new RespVo<>("Path error, please refresh and try again", false, "Path error, please refresh and try again");
        }
        try {
            // 使用Files.walk()递归遍历文件夹中的所有文件和子文件夹
            Files.walk(folder)
                    .filter(path -> !Files.isDirectory(path)) // 过滤掉文件夹，只保留文件
                    .forEach(path -> {
                        try {
                            Files.delete(path); // 删除文件
                            log.info("Clear successfully path【{}】", path);
                        } catch (IOException e) {
                            log.error("Clearing failed", e);
                        }
                    });
            return new RespVo<>("Clear successfully");
        } catch (IOException e) {
            log.error("Clearing failed", e);
            return new RespVo<>("Clearing failed", false, e.getMessage());
        }
    }
}
