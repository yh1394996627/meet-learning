package org.example.meetlearning.service;


import cn.hutool.core.util.BooleanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.converter.UserFinanceConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.*;
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
    private final TeacherService teacherService;
    private final UserFinanceService userFinanceService;
    private final UserFinanceRecordService userFinanceRecordService;

    public RespVo<String> manageRegister(UserManageOperaReqVo reqVo) {
        try {
            User accountUser = userService.selectByAccountCode(reqVo.getAccountCode());
            Assert.isTrue(accountUser == null, "账号【" + reqVo.getAccountCode() + "】已存在无法注册");
            User user = UserConverter.INSTANCE.toCreateManage(reqVo);
            userService.insertEntity(user);
            addFinance("SYSTEM", "SYSTEM", user);
        } catch (Exception ex) {
            log.error("注册失败", ex);
            return new RespVo<>("注册失败", false, ex.getMessage());
        }
        return new RespVo<>("注册成功");
    }

    public RespVo<UserInfoRespVo> loginUser(UserLoginReqVo reqVo) {
        try {
            User accountUser = userService.selectByLogin(reqVo.getAccountCode(), MD5Util.md5("MD5", reqVo.getPassword()));
            Assert.notNull(accountUser, "Account does not exist or account password is incorrect");
            if (BooleanUtil.isTrue(reqVo.getManage())) {
                List<String> types = List.of(RoleEnum.MANAGER.name(), RoleEnum.TEACHER.name(), RoleEnum.AFFILIATE.name());
                Assert.isTrue(types.contains(accountUser.getType()), "Account password incorrect");
            } else if (BooleanUtil.isFalse(reqVo.getManage())) {
                List<String> types = List.of(RoleEnum.STUDENT.name());
                Assert.isTrue(types.contains(accountUser.getType()), "Account password incorrect");
            }
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
    public RespVo<String> uploadPcAvatar(String userCode, String userId, MultipartFile file) {
        userCode = StringUtils.isNotEmpty(userId) ? userId : userCode;
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        String url = uploadAvatar(userCode, file);
        Teacher teacher = teacherService.selectByRecordId(userCode);
        teacher.setAvatarUrl(url);
        teacherService.updateEntity(teacher);
        return new RespVo<>(downloadAvatar(url));
    }

    public RespVo<String> uploadPcVideo(String userCode, String userId, MultipartFile file) {
        userCode = StringUtils.isNotEmpty(userId) ? userId : userCode;
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        String url = uploadVideo(userCode, file);
        Teacher teacher = teacherService.selectByRecordId(userCode);
        teacher.setVideoUrl(url);
        teacherService.updateEntity(teacher);
        return new RespVo<>(downloadAvatar(url));
    }

    public RespVo<FileRecordVo> uploadPcFile(String userCode, String userId, MultipartFile file) {
        userCode = StringUtils.isNotEmpty(userId) ? userId : userCode;
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        return uploadCertificate(userCode, file);
    }

    public RespVo<String> deletedPcFile(String userCode, FileRecordVo fileRecordVo) {
        User accountUser = userService.selectByRecordId(userCode);
        Assert.notNull(accountUser, "User information not obtained");
        return deletedFile(userCode, fileRecordVo);
    }


    public RespVo<String> studentPay(String userCode, String userName, UserPayReqVo reqVo) {
        //支付用戶减掉课时币
        financeTokenLogs(userCode, userName, userCode, reqVo.getQuantity().negate(), reqVo);
        //接收用戶加课时币
        financeTokenLogs(userCode, userName, reqVo.getUserId(), reqVo.getQuantity(), reqVo);
        return new RespVo<>("Payment successful");
    }

    public RespVo<PageVo<UserStudentPayRecordRespVo>> studentPayRecord(UserStudentFinanceRecordQueryVo reqVo) {
        //更新 userFinance
        Page<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectByParams(reqVo.getParams(), reqVo.getPageRequest());
        PageVo<UserStudentPayRecordRespVo> pageVo = PageVo.map(userFinanceRecordList, UserFinanceConverter.INSTANCE::toUserStudentPayRecordRespVo);
        return new RespVo<>(pageVo);
    }

    public RespVo<UserStudentPayInfoVo> studentPayInfo(String userCode, RecordIdQueryVo queryVo) {
        UserFinance manageFinance = userFinanceService.selectByUserId(userCode);
        Assert.notNull(manageFinance, "User Finance information not obtained");
        User user = userService.selectByRecordId(queryVo.getRecordId());
        Assert.notNull(user, "User information not obtained");
        UserStudentPayInfoVo respVo = new UserStudentPayInfoVo();
        respVo.setUserId(user.getRecordId());
        respVo.setName(user.getName());
        respVo.setEmail(user.getEmail());
        respVo.setBalanceQty(BigDecimalUtil.nullOrZero(manageFinance.getBalanceQty()));
        return new RespVo<>(respVo);
    }

}
