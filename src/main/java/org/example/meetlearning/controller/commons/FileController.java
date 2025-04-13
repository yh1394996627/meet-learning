package org.example.meetlearning.controller.commons;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.example.meetlearning.service.FileService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.file.FileInfoReqVo;
import org.example.meetlearning.vo.file.FilePageRespVo;
import org.example.meetlearning.vo.file.FileQueryVo;
import org.springframework.core.io.Resource;
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




}
