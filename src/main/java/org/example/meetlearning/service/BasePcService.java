package org.example.meetlearning.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.common.OssConfig;
import org.example.meetlearning.converter.FileRecordConverter;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.dao.entity.FileRecord;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.FileRecordService;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.*;

@Service
@Slf4j
public class BasePcService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FileRecordService fileRecordService;

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
     * 批量上传证书用于老师
     */
    @Transactional(rollbackFor = Exception.class)
    public RespVo<List<FileRecordVo>> uploadCertificate(String userCode, List<MultipartFile> files) {
        // 查询已存在的文件记录
        // List<FileRecord> fileRecords = fileRecordService.selectByUserId(userCode);
        try {
            List<FileRecord> newFileRecords = new ArrayList<>();
            for (MultipartFile file : files) {
                String fileName = "certificate/" + UUID.randomUUID() + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
                ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
                //添加记录
                newFileRecords.add(FileRecordConverter.INSTANCE.toCreate(userCode, file.getName(), fileName));
            }
            //批量保存
            fileRecordService.insertBatch(newFileRecords);
            List<FileRecord> fileRecords = fileRecordService.selectByUserId(userCode);
            return new RespVo<>(fileRecords.stream().map(FileRecordConverter.INSTANCE::toFileRecordVo).toList());
        } catch (Exception e) {
            log.error("File retrieval failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }


    @Transactional(rollbackFor = Exception.class)
    public RespVo<List<FileRecordVo>> deletedFile(String userCode, List<FileRecordVo> fileRecordVos) {
        try {
            for (FileRecordVo fileRecordVo : fileRecordVos) {
                fileRecordService.deleteByRecordId(fileRecordVo.getRecordId());
            }
            List<FileRecord> fileRecords = fileRecordService.selectByUserId(userCode);
            return new RespVo<>(fileRecords.stream().map(FileRecordConverter.INSTANCE::toFileRecordVo).toList());
        } catch (Exception e) {
            log.error("File retrieval failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }


    public String getLastPartOfString(String str) {
        int lastIndex = str.lastIndexOf('.');
        if (lastIndex != -1) {
            return str.substring(lastIndex + 1);
        } else {
            return ""; // 或者返回 null，或者原样返回 str，取决于你的需求
        }
    }
}
