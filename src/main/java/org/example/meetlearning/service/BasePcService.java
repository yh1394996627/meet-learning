package org.example.meetlearning.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.common.OssConfig;
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
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class BasePcService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private OssConfig ossConfig;

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
     * @param email      邮箱地址
     * @param verifyCode 验证码
     */
    public void emailVerify(String email, String verifyCode) {
        String key = RedisCommonsUtil.emailKeyGet(email);
        Object redisObj = redisTemplate.opsForValue().get(key);
        Assert.isTrue(redisObj != null && redisObj.equals(verifyCode), "Verification code error");
    }


    /**
     * 下载头像
     */
    public URL downloadAvatar(String fileName) {
        try {
            if (StringUtils.isEmpty(fileName)) {
                return null;
            }
            URL url = ossConfig.getOssClient().generatePresignedUrl(ossConfig.getBucketName(), fileName, new Date(new Date().getTime() + 3600000));
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传头像
     */
    public URL uploadAvatar(String userCode, MultipartFile file) {
        String fileName = "avatar/" + userCode + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            return downloadAvatar(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传个人视频
     */
    public URL uploadVideo(String userCode, MultipartFile file) {
        String fileName = "video/" + userCode + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            return downloadAvatar(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 上传证书用于老师
     *
     * @param userCode
     * @param file
     * @return
     *//*
    public List<URL> uploadCertificate(String userCode, List<MultipartFile> file) {
        String fileName = "certificate/" + userCode + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            return downloadAvatar(fileName);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }*/


    public String getLastPartOfString(String str) {
        int lastIndex = str.lastIndexOf('.');
        if (lastIndex != -1) {
            return str.substring(lastIndex + 1);
        } else {
            return ""; // 或者返回 null，或者原样返回 str，取决于你的需求
        }
    }
}
