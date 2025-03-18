package org.example.meetlearning.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.UserPcService;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.UserInfoRespVo;
import org.example.meetlearning.vo.user.UserLoginReqVo;
import org.example.meetlearning.vo.user.UserManageOperaReqVo;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Tag(name = "用户接口")
@RestController
@Slf4j
@AllArgsConstructor
public class UserController implements BaseController {

    private final UserPcService userPcService;

    @Operation(summary = "管理员注册", operationId = "manageRegister")
    @PostMapping(value = "v1/user/manager/register")
    public RespVo<String> manageRegister(@RequestBody UserManageOperaReqVo reqVo) {
        return userPcService.manageRegister(reqVo);
    }


    @Operation(summary = "管理系统用户登录", operationId = "login")
    @PostMapping(value = "v1/user/login")
    public RespVo<UserInfoRespVo> login(@RequestBody UserLoginReqVo reqVo) {
        return userPcService.loginUser(reqVo);
    }


    @Operation(summary = "用户信息查询", operationId = "login")
    @PostMapping(value = "v1/user/info")
    public RespVo<UserInfoRespVo> userInfo() {
        return userPcService.userInfo(getUserCode());
    }


    @Operation(summary = "上传头像", operationId = "uploadAvatar")
    @PostMapping(value = "v1/user/upload/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<URL> uploadAvatar(@RequestPart("file") MultipartFile file) {
        return userPcService.uploadPcAvatar(getUserCode(), file);
    }

    @Operation(summary = "上传视频", operationId = "uploadVideo")
    @PostMapping(value = "v1/user/upload/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<URL> uploadVideo(@RequestPart("file") MultipartFile file) {
        return userPcService.uploadPcVideo(getUserCode(), file);
    }

    @Operation(summary = "上传证书", operationId = "uploadFile")
    @PostMapping(value = "v1/user/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<List<FileRecordVo>> uploadFile(@RequestBody List<MultipartFile> file) {
        return userPcService.uploadPcFile(getUserCode(), file);
    }

    @Operation(summary = "", operationId = "deleteFile")
    @PostMapping(value = "v1/user/delete/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<List<FileRecordVo>> deleteFile(@RequestBody List<FileRecordVo> file) {
        return userPcService.deletedFile(getUserCode(), file);
    }

}
