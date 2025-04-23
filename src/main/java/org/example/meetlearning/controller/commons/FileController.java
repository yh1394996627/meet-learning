package org.example.meetlearning.controller.commons;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.service.FileService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.file.FileInfoReqVo;
import org.example.meetlearning.vo.file.FilePageRespVo;
import org.example.meetlearning.vo.file.FileQueryVo;
import org.example.meetlearning.vo.file.FileVideoReqVo;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Tag(name = "文件接口")
@RestController
@AllArgsConstructor
public class FileController {

    private final FileService fileService;

    @Operation(summary = "文件列表", operationId = "filePage")
    @PostMapping(value = "v1/file/page")
    public RespVo<PageVo<FilePageRespVo>> filePage(@RequestBody FileQueryVo queryVo) {
        List<FilePageRespVo> filePageRespVos = fileService.listFiles(queryVo);
        filePageRespVos = filePageRespVos.stream().sorted(Comparator.comparing(FilePageRespVo::getLastModified)).collect(Collectors.toList());
        Page<FilePageRespVo> page = new Page<>(queryVo.getCurrent(), queryVo.getPageSize(), filePageRespVos.size());
        page.setRecords(filePageRespVos);
        PageVo<FilePageRespVo> pageVo = PageVo.map(page, list -> list);
        return new RespVo<>(pageVo);
    }

    @Operation(summary = "查询文件信息", operationId = "getBinaryFile")
    @PostMapping(value = "v1/file/info")
    public ResponseEntity<StreamingResponseBody> getBinaryFile(@RequestBody FileInfoReqVo fileReqVo) {
        return fileService.getBinaryFile(fileReqVo.getPath());
    }

    @Operation(summary = "上传文件", operationId = "uploadFolder")
    @PostMapping(value = "v1/file/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<String> uploadFolder(@RequestParam("path") String path, @RequestPart("file") MultipartFile file) {
        return fileService.uploadFolder(path, file);
    }

    @Operation(summary = "清空私人文件夹", operationId = "removeDic")
    @PostMapping(value = "v1/file/delete")
    public RespVo<String> removeDic(@RequestBody FileInfoReqVo reqVo) {
        return fileService.removeDic(reqVo.getPath());
    }

    @Operation(summary = "公共删除文件", operationId = "deletedFolderOrFile")
    @PostMapping(value = "v1/file/public/deleted")
    public RespVo<String> deletedFolderOrFile(@RequestBody FileInfoReqVo reqVo) {
        fileService.deletedFolderOrFile(reqVo.getPath());
        return new RespVo<>("Delete successfully");
    }
    @Operation(summary = "公共创建文件夹", operationId = "createFolder")
    @PostMapping(value = "v1/file/create/folder")
    public RespVo<String> createFolder(@RequestBody FileInfoReqVo reqVo) {
        return fileService.createFolder(reqVo.getPath());
    }

    @Operation(summary = "公共上传文件", operationId = "uploadFolder")
    @PostMapping(value = "v1/file/public/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<String> uploadPublicFolder(@RequestParam("path") String path, @RequestPart("file") MultipartFile file) {
        return fileService.uploadPublicFolder(path, file);
    }

    @Operation(summary = "视频转换和格式返回前段", operationId = "getVideoUrl")
    @PostMapping("v1/file/url")
    public ResponseEntity<String> getVideoUrl(@RequestBody FileVideoReqVo reqVo) {
        if (StringUtils.isEmpty(reqVo.getFilePath())) {
            return ResponseEntity.ok("");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "video/mp4"); // 告诉浏览器这是视频
        headers.add("Content-Disposition", "inline; filename=\"video.mp4\""); // 内联播放，而非下载
        return ResponseEntity.ok().headers(headers).body(reqVo.getFilePath());
    }
}
