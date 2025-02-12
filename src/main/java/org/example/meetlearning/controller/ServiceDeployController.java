package org.example.meetlearning.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.meetlearning.service.ServiceDeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "前端发包服务")
@RestController
public class ServiceDeployController {

    @Autowired
    private ServiceDeployService serviceDeployService;

    @Operation(summary = "上传ZIP文件到服务器", operationId = "uploadFolder")
    @PostMapping(value = "v1/deploy/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadFolder(@RequestParam("type") Integer type, @RequestPart("file") MultipartFile file) {
        return serviceDeployService.uploadFolder(type, file);
    }

}
