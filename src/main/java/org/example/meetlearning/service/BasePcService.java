package org.example.meetlearning.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@Slf4j
public class BasePcService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    /**
     * 创建登陆帐号。学生 老师 代理商来源
     */
    public void addUser(String userCode, String userName, String recordId, String accountCode,
                        String password, RoleEnum roleType, String name, String enName, String email) {
        User user = userService.selectByRecordId(recordId);
        Assert.isNull(user, "The user already exists and cannot be added");

        user = UserConverter.INSTANCE.toCreateUser(userCode, userName, recordId, accountCode,
                password, roleType, name, enName, email);
        userService.insertEntity(user);
        log.error("Login account successfully added");
    }


    /**
     * 邮件验证码校验
     *
     * @param email 邮箱地址
     * @param verifyCode 验证码
     */
    public void emailVerify(String email, String verifyCode) {
        String key = RedisCommonsUtil.emailKeyGet(email);
        Object redisObj = redisTemplate.opsForValue().get(key);
        Assert.isTrue(redisObj != null && redisObj.equals(verifyCode), "Verification code error");
    }
}
