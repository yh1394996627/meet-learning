package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.common.OssConfig;
import org.example.meetlearning.converter.FileRecordConverter;
import org.example.meetlearning.converter.TokenConverter;
import org.example.meetlearning.converter.UserConverter;
import org.example.meetlearning.converter.UserFinanceConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.example.meetlearning.vo.common.FileRecordVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.user.UserPayReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.net.URL;
import java.util.*;

@Service
@Slf4j
@Transactional
public class BasePcService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private UserService userService;

    @Autowired
    private FileRecordService fileRecordService;

    @Autowired
    private UserFinanceService userFinanceService;

    @Autowired
    private UserFinanceRecordService userFinanceRecordService;

    @Autowired
    private OssConfig ossConfig;

    @Autowired
    private BaseConfigService baseConfigService;

    @Autowired
    private TokensLogService tokensLogService;


    /**
     * 创建登陆帐号。学生 老师 代理商来源
     */
    public User addUser(String userCode, String userName, String recordId, String accountCode,
                        String password, RoleEnum roleType, String name, String enName, String email) {
        User user = userService.selectByAccountCode(email);
        Assert.isNull(user, "The user already exists and cannot be added");

        user = UserConverter.INSTANCE.toCreateUser(userCode, userName, recordId, accountCode,
                password, roleType, name, enName, email);
        userService.insertEntity(user);
        log.info("Login account successfully added recordId:{}", recordId);
        return user;
    }


    /**
     * 删除登陆帐号和余额信息
     */
    public void deleteUser(String recordId) {
        UserFinance userFinance = userFinanceService.selectByUserId(recordId);
        Assert.isTrue(userFinance != null && BigDecimalUtil.eqZero(userFinance.getBalanceQty()), "The user has a balance that cannot be deleted");
        userService.deleteByRecordId(recordId);
        log.info("Login account delete successfully recordId:{}", recordId);
        userFinanceService.deleteByUserId(recordId);
        log.info("Finance balance delete successfully recordId:{}", recordId);
    }

    public void addFinance(String userCode, String userName, User user) {
        UserFinance userFinance = UserFinanceConverter.INSTANCE.toCreate(userCode, userName, user);
        userFinanceService.insertEntity(userFinance);
        log.info("User Finance successfully added");
    }


    /**
     * 邮件验证码校验
     *
     * @param email      邮箱地址
     * @param verifyCode 验证码
     */
    public void emailVerify(String email, String verifyCode) {
        String key = RedisCommonsUtil.emailKeyGet(email);
        log.info("Redis email:{}", email);
        Object redisObj = redisTemplate.opsForValue().get(key);
        log.info("Redis key:{}", key);
        log.info("Redis redisObj:{}", redisObj);
        Assert.isTrue(redisObj != null && StringUtils.equals(verifyCode, redisObj.toString()), "Verification code error");
    }


    /**
     * 下载头像
     */
    public String downloadAvatar(String fileName) {
        try {
            if (StringUtils.isEmpty(fileName)) {
                return null;
            }
            URL url = ossConfig.getOssClient().generatePresignedUrl(ossConfig.getBucketName(), fileName, new Date(new Date().getTime() + 3600000));
            return url != null ? url.toString().replace("%2F", "/") : null;
        } catch (Exception e) {
            log.error("Download failed！");
            return null;
        }
    }


    /**
     * 上传头像
     */
    public String uploadAvatar(String userCode, MultipartFile file) {
        String fileName = "avatar/" + userCode + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 上传个人视频
     */
    public String uploadVideo(String userCode, MultipartFile file) {
        String fileName = "video/" + userCode + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            return fileName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 批量上传证书用于老师
     */
    @Transactional(rollbackFor = Exception.class)
    public RespVo<FileRecordVo> uploadCertificate(String userCode, MultipartFile file) {
        try {
            String fileName = "certificate/" + UUID.randomUUID() + "." + getLastPartOfString(Objects.requireNonNull(file.getOriginalFilename()));
            ossConfig.getOssClient().putObject(ossConfig.getBucketName(), fileName, file.getInputStream());
            //批量保存
            FileRecord record = FileRecordConverter.INSTANCE.toCreate(userCode, fileName, file.getOriginalFilename());
            fileRecordService.insertBatch(List.of(record));
            FileRecordVo recordVo = FileRecordConverter.INSTANCE.toFileRecordVo(record);
            recordVo.setFileUrl(downloadAvatar(fileName));
            return new RespVo<>(recordVo);
        } catch (Exception e) {
            log.error("File retrieval failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }

    /**
     * 查询文件列表
     */
    public List<FileRecordVo> getFileRecordVoList(String userCode) {
        List<FileRecord> fileRecords = fileRecordService.selectByUserId(userCode);
        return fileRecords.stream().map(item -> {
            FileRecordVo fileRecordVo = FileRecordConverter.INSTANCE.toFileRecordVo(item);
            fileRecordVo.setFileUrl(downloadAvatar(fileRecordVo.getFileUrl()));
            return fileRecordVo;
        }).toList();
    }


    @Transactional(rollbackFor = Exception.class)
    public RespVo<String> deletedFile(String userCode, FileRecordVo fileRecordVo) {
        try {
            fileRecordService.deleteByRecordId(fileRecordVo.getRecordId());
            return new RespVo<>("Attachment deleted successfully");
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

    //充值消费接口
    public void financeTokenLogs(String userCode, String userName, String userId, BigDecimal quantity, UserPayReqVo reqVo) {
        reqVo.setUserId(userId);
        User user = userService.selectByRecordId(userId);
        Assert.notNull(user, "user does not exist userId:" + userId);
        UserFinance userFinance = userFinanceService.selectByUserId(userId);
        Assert.notNull(userFinance, "To obtain management financial information");
        List<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectByUserId(userId);
        BigDecimal balanceQty = userFinance.getBalanceQty();
        BigDecimal usedQty = userFinanceRecordList.stream().map(UserFinanceRecord::getUsedQty).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal balance = BigDecimalUtil.add(balanceQty, quantity);
        Assert.isTrue(BigDecimalUtil.gteZero(balance), "Insufficient balance");


        //更新 userFinance
        userFinance.setBalanceQty(balance);
        if (BigDecimalUtil.gtZero(quantity)) {
            if (StringUtils.equals(user.getType(), RoleEnum.STUDENT.name()) || StringUtils.equals(user.getType(), RoleEnum.AFFILIATE.name())) {
                //新增用户课时币记录 userFinanceRecord
                UserFinanceRecord userFinanceRecord = UserFinanceConverter.INSTANCE.toCreateRecord(userCode, userName, reqVo);
                userFinanceRecord.setBalanceQty(userFinance.getBalanceQty().add(reqVo.getQuantity()));
                userFinanceRecordService.insertEntity(userFinanceRecord);
            }
            //添加记录课时币记录
            TokensLog tokensLog = TokenConverter.INSTANCE.toCreateTokenByFinanceRecord(userCode, userName, userFinance, user, quantity, reqVo.getPayAmount(), reqVo.getRemark());
            if (!StringUtils.isEmpty(reqVo.getCurrencyCode())) {
                BaseConfig baseConfig = baseConfigService.selectByCode(reqVo.getCurrencyCode());
                Assert.notNull(baseConfig, "Configuration information not obtained record:【" + reqVo.getCurrencyCode() + "】");
                tokensLog.setCurrencyCode(baseConfig.getCode());
                tokensLog.setCurrencyName(baseConfig.getName());
            }
            tokensLogService.insertEntity(tokensLog);
        } else {
            //已消费金额更新
            userFinance.setConsumptionQty(BigDecimalUtil.add(usedQty, quantity.abs()));
            if (StringUtils.equals(user.getType(), RoleEnum.STUDENT.name())) {
                userFinanceRecordList.forEach(f -> {
                    if (f.getExpirationTime() == null) {
                        f.setExpirationTime(DateUtil.parse("2099-12-31", "yyyy-MM-dd"));
                    }
                });
                userFinanceRecordList = userFinanceRecordList.stream().sorted(Comparator.comparing(UserFinanceRecord::getExpirationTime)).toList();
                if (!CollectionUtils.isEmpty(userFinanceRecordList)) {
                    UserFinanceRecord userFinanceRecord = userFinanceRecordList.get(0);
                    userFinanceRecord.setCanQty(BigDecimalUtil.sub(userFinanceRecord.getCanQty(), quantity.abs()));
                    userFinanceRecord.setUsedQty(BigDecimalUtil.add(userFinanceRecord.getCanQty(), quantity.abs()));
                    userFinanceRecord.setBalanceQty(userFinance.getBalanceQty().add(reqVo.getQuantity()));
                    userFinanceRecordService.updateByEntity(userFinanceRecord);
                }
            }

            TokensLog tokensLog = TokenConverter.INSTANCE.toCreateTokenByFinanceRecord(userCode, userName, userFinance, user, quantity, reqVo.getPayAmount(), reqVo.getRemark());
            if (!StringUtils.isEmpty(reqVo.getCurrencyCode())) {
                BaseConfig baseConfig = baseConfigService.selectByCode(reqVo.getCurrencyCode());
                Assert.notNull(baseConfig, "Configuration information not obtained record:【" + reqVo.getCurrencyCode() + "】");
                tokensLog.setCurrencyCode(baseConfig.getCode());
                tokensLog.setCurrencyName(baseConfig.getName());
            }
            tokensLogService.insertEntity(tokensLog);
        }
        userFinanceService.updateByEntity(userFinance);
    }


}
