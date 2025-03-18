package org.example.meetlearning.service;


import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.StudentService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.UserInfoRespVo;
import org.example.meetlearning.vo.user.UserLoginReqVo;
import org.example.meetlearning.vo.user.UserManageOperaReqVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class UserPcService extends BasePcService {

    private final UserService userService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    public RespVo<String> manageRegister(UserManageOperaReqVo reqVo) {
        try {
            User accountUser = userService.selectByAccountCode(reqVo.getAccountCode());
            Assert.isTrue(accountUser == null, "账号【" + reqVo.getAccountCode() + "】已存在无法注册");
            User user = UserConverter.INSTANCE.toCreateManage(reqVo);
            userService.insertEntity(user);
        } catch (Exception ex) {
            log.error("注册失败", ex);
            return new RespVo<>("注册失败", false, ex.getMessage());
        }
        return new RespVo<>("注册成功");
    }

    public RespVo<UserInfoRespVo> loginUser(UserLoginReqVo reqVo) {
        try {
            User accountUser = userService.selectByLogin(reqVo.getAccountCode(), MD5Util.md5("MD5", reqVo.getPassword()));
            Assert.notNull(accountUser, "账号或密码错误！");
            UserInfoRespVo respVo = UserConverter.INSTANCE.toUserInfoRespVo(accountUser);
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("登陆失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }

    }


    public RespVo<UserInfoRespVo> userInfo(String userCode) {
        try {
            User accountUser = userService.selectByRecordId(userCode);
            Assert.notNull(accountUser, "User information not obtained");
            UserInfoRespVo respVo = UserConverter.INSTANCE.toUserInfoRespVo(accountUser);
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("查询个人信息失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

    /**
     * 上传头像
     */
    public RespVo<URL> uploadPcAvatar(String userCode, MultipartFile file) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        URL url = uploadAvatar(userCode, file);
        Teacher teacher = teacherService.selectByRecordId(userCode);
        teacher.setAvatarUrl(url.getPath());
        teacherService.updateEntity(teacher);
        return new RespVo<>(url);
    }

    public RespVo<URL> uploadPcVideo(String userCode, MultipartFile file) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        URL url = uploadVideo(userCode, file);
        Teacher teacher = teacherService.selectByRecordId(userCode);
        teacher.setVideoUrl(url.getPath());
        teacherService.updateEntity(teacher);
        return new RespVo<>(url);
    }

    public RespVo<List<FileRecordVo>> uploadPcFile(String userCode, List<MultipartFile> file) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        return uploadCertificate(userCode, file);
    }


    public RespVo<List<FileRecordVo>> deletedPcFile(String userCode, List<FileRecordVo> fileRecordVos) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        return deletedFile(userCode, fileRecordVos);
    }


}
