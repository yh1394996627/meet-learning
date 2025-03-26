package org.example.meetlearning.controller.user;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.UserPcService;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.*;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
    public RespVo<String> uploadAvatar(@RequestParam(value = "userId", required = false) String userId, @RequestPart("file") MultipartFile file) {
        return userPcService.uploadPcAvatar(getUserCode(), userId, file);
    }

    @Operation(summary = "上传视频", operationId = "uploadVideo")
    @PostMapping(value = "v1/user/upload/video", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<String> uploadVideo(@RequestParam(value = "userId", required = false) String userId, @RequestPart("file") MultipartFile file) {
        return userPcService.uploadPcVideo(getUserCode(), userId, file);
    }

    @Operation(summary = "上传证书", operationId = "uploadFile")
    @PostMapping(value = "v1/user/upload/file", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public RespVo<FileRecordVo> uploadFile(@RequestParam(value = "userId", required = false) String userId, @RequestPart("file") MultipartFile file) {
        return userPcService.uploadPcFile(getUserCode(), userId, file);
    }

    @Operation(summary = "删除附件", operationId = "deleteFile")
    @PostMapping(value = "v1/user/delete/file")
    public RespVo<String> deleteFile(@RequestBody FileRecordVo file) {
        return userPcService.deletedFile(getUserCode(), file);
    }

    @Operation(summary = "充值接口", operationId = "studentPay")
    @PostMapping(value = "v1/user/pay")
    public RespVo<String> studentPay(@RequestBody UserPayReqVo reqVo) {
        return userPcService.studentPay(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "充值接口详细", operationId = "studentPayInfo")
    @PostMapping(value = "v1/user/pay/info")
    public RespVo<UserStudentPayInfoVo> studentPayInfo(@RequestBody RecordIdQueryVo queryVo) {
        return userPcService.studentPayInfo(getUserCode(), queryVo);
    }

    @Operation(summary = "用户课时币记录", operationId = "studentPay")
    @PostMapping(value = "v1/user/finance/record")
    public RespVo<PageVo<UserStudentPayRecordRespVo>> studentFinanceRecord(@RequestBody UserStudentFinanceRecordQueryVo queryVo) {
        return userPcService.studentPayRecord(queryVo);
    }

}
