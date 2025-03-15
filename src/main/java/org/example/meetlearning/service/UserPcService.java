package org.example.meetlearning.service;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.UserInfoRespVo;
import org.example.meetlearning.vo.user.UserLoginReqVo;
import org.example.meetlearning.vo.user.UserManageOperaReqVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
@Slf4j
public class UserPcService {

    private final UserService userService;

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
            Assert.notNull(accountUser, "未获取到客户信息！");
            UserInfoRespVo respVo = UserConverter.INSTANCE.toUserInfoRespVo(accountUser);
            return new RespVo<>(respVo);
        } catch (Exception ex) {
            log.error("查询个人信息失败", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

}
