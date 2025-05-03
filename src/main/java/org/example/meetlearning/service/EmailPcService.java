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
            String htmlBody = verifyStr(String.valueOf(verificationCode));
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



    public String verifyStr(String verificationCode){
        return "<div style='"
                + "max-width: 600px;"
                + "margin: 20px auto;"
                + "padding: 30px;"
                + "font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;"
                + "background-color: #f7f7f7;"
                + "border-radius: 8px;"
                + "box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>"

                + "<div style='text-align: center; margin-bottom: 25px;'>"
                + "<img src='[您的LOGO地址]' alt='公司Logo' style='max-height: 50px;'>"
                + "</div>"

                + "<h1 style='"
                + "color: #333333;"
                + "font-size: 24px;"
                + "margin: 0 0 20px 0;"
                + "text-align: center;'>"
                + "邮箱验证码"
                + "</h1>"

                + "<div style='"
                + "background: #ffffff;"
                + "padding: 25px;"
                + "border-radius: 6px;"
                + "border: 1px solid #eeeeee;"
                + "margin-bottom: 25px;'>"

                + "<p style='"
                + "color: #666666;"
                + "font-size: 15px;"
                + "line-height: 1.6;"
                + "margin: 0 0 15px 0;'>"
                + "您的验证码是："
                + "</p>"

                + "<div style='"
                + "background: #f8f9fa;"
                + "padding: 15px;"
                + "border-radius: 4px;"
                + "text-align: center;"
                + "font-size: 28px;"
                + "letter-spacing: 3px;"
                + "color: #2c7be5;"
                + "font-weight: bold;"
                + "margin: 20px 0;'>"
                + verificationCode
                + "</div>"

                + "<p style='"
                + "color: #999999;"
                + "font-size: 13px;"
                + "line-height: 1.5;"
                + "margin: 15px 0 0 0;'>"
                + "温馨提示："
                + "<br>1. 该验证码 <strong style='color:#e74c3c'>" + 5 + "分钟内</strong> 有效"
                + "<br>2. 请勿将验证码透露给他人"
                + "<br>3. 如非本人操作，请忽略本邮件"
                + "</p>"
                + "</div>"

                + "<div style='"
                + "text-align: center;"
                + "color: #999999;"
                + "font-size: 12px;"
                + "padding-top: 20px;"
                + "border-top: 1px solid #eeeeee;'>"
                + "<p style='margin:5px 0;'>[公司名称]</p>"
                + "<p style='margin:5px 0;'>服务热线：[联系电话]</p>"
                + "</div>"
                + "</div>";
    }
}