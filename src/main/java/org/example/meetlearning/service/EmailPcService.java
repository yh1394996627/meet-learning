package org.example.meetlearning.service;

import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.dm20151123.models.SingleSendMailResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class EmailPcService extends BasePcService {

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${mail.accessKeyId}")
    private String accessKeyId;

    @Value("${mail.accessKeySecret}")
    private String accessKeySecret;

    @Value("${mail.accountName}")
    private String accountName;

    @Value("${mail.fromAlias}")
    private String fromAlias;

    @Value("${mail.subject}")
    private String subject;


    public RespVo<String> sendVerificationEmail(String userCode, String toEmail) throws Exception {
        try {
            int verificationCode = (int) (Math.random() * 900000) + 100000;
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            config.endpoint = "dm.aliyuncs.com";

            Client client = new Client(config);

            // 动态替换模板变量
            String htmlBody = "<h1>您的验证码是：</h1><p>" + verificationCode + "</p>";
            SingleSendMailRequest request = new SingleSendMailRequest()
                    .setAccountName(accountName)
                    .setFromAlias(fromAlias)
                    .setAddressType(1)
                    .setToAddress(toEmail)
                    .setSubject(subject)
                    .setHtmlBody(htmlBody)
                    .setReplyToAddress(true);
            SingleSendMailResponse response = client.singleSendMailWithOptions(request, new RuntimeOptions());
            redisTemplate.opsForValue().set(RedisCommonsUtil.emailKeyGet(toEmail), verificationCode, 300, TimeUnit.SECONDS);
            log.info("发送成功-code:{}", redisTemplate.opsForValue().get(RedisCommonsUtil.emailKeyGet(toEmail)));
            return new RespVo<>("发送成功");
        } catch (Exception e) {
            log.error("发送失败", e);
            return new RespVo<>("发送失败", false, e.getMessage());
        }
    }


    public void sendReEmail(String accessToken, String toEmail){
        try {
            int verificationCode = (int) (Math.random() * 900000) + 100000;
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            config.endpoint = "dm.aliyuncs.com";

            Client client = new Client(config);

            // 动态替换模板变量
            String htmlBody = String.format(
                    "请点击以下链接完成ZOOM账号注册: https://classx-cc.zoom.us/activate_help?code=%s\n\n" +
                            "注册完成后，您将可以使用我们的会议预约服务。", accessToken);
            SingleSendMailRequest request = new SingleSendMailRequest()
                    .setAccountName(accountName)
                    .setFromAlias(fromAlias)
                    .setAddressType(1)
                    .setToAddress(toEmail)
                    .setSubject(subject)
                    .setHtmlBody(htmlBody)
                    .setReplyToAddress(true);
            SingleSendMailResponse response = client.singleSendMailWithOptions(request, new RuntimeOptions());
            redisTemplate.opsForValue().set(RedisCommonsUtil.emailKeyGet(toEmail), verificationCode, 300, TimeUnit.SECONDS);
            log.info("发送成功-code:{}", redisTemplate.opsForValue().get(RedisCommonsUtil.emailKeyGet(toEmail)));
        } catch (Exception e) {
            log.error("发送失败", e);
        }
    }


}