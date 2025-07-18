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
import org.springframework.stereotype.Service;

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
    }


    public String verifyStr(String verificationCode) {
        return "<div style='"
                + "max-width: 600px;"
                + "margin: 20px auto;"
                + "padding: 30px;"
                + "font-family: \"Helvetica Neue\", Helvetica, Arial, sans-serif;"
                + "background-color: #f7f7f7;"
                + "border-radius: 8px;"
                + "box-shadow: 0 2px 10px rgba(0,0,0,0.1);'>"

                + "<div style='text-align: center; margin-bottom: 25px;'>"
                + "<img src='/static/logo.jpg' alt='公司Logo' style='max-height: 50px;'>"
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
                + "<p style='margin:5px 0;'>中菲（广东）国际文化有限公司</p>"
                + "</div>"
                + "</div>";
    }


    public RespVo<String> sendNotice(String time, String teacherName, String joinUrl, String toEmail) {
        try {
            int verificationCode = (int) (Math.random() * 900000) + 100000;
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            config.endpoint = "dm.aliyuncs.com";

            Client client = new Client(config);

            // 动态替换模板变量
            String htmlBody = sendNoticeStr(time, teacherName, joinUrl);
            SingleSendMailRequest request = new SingleSendMailRequest()
                    .setAccountName(accountName)
                    .setFromAlias(fromAlias)
                    .setAddressType(1)
                    .setToAddress(toEmail)
                    .setSubject(subject)
                    .setHtmlBody(htmlBody)
                    .setReplyToAddress(true);
            SingleSendMailResponse response = client.singleSendMailWithOptions(request, new RuntimeOptions());
            return new RespVo<>("发送成功");
        } catch (Exception e) {
            log.error("发送失败", e);
            return new RespVo<>("发送失败", false, e.getMessage());
        }
    }

    public String sendNoticeStr(String time, String teacherName, String joinUrl) {
        return "<div style=\""
                + "max-width: 600px;"
                + "margin: 20px auto;"
                + "padding: 30px;"
                + "font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif;"
                + "background-color: #f7f7f7;"
                + "border-radius: 8px;"
                + "box-shadow: 0 2px 10px rgba(0,0,0,0.1);\">"

                + "<h1 style=\""
                + "color: #2c7be5;"
                + "font-size: 24px;"
                + "margin: 0 0 25px 0;"
                + "text-align: center;"
                + "padding-bottom: 15px;\">"
                + "课程提醒"
                + "</h1>"

                + "<div style=\""
                + "background: #ffffff;"
                + "padding: 25px;"
                + "border-radius: 6px;"
                + "border: 1px solid #eeeeee;\">"

                + "<div style=\"margin-bottom: 20px;\">"
                + "<p style=\""
                + "color: #666666;"
                + "font-size: 16px;"
                + "margin: 10px 0;\">"
                + "<strong>上课时间：" + time + "</p>"
                + "<p style=\""
                + "color: #666666;"
                + "font-size: 16px;"
                + "margin: 10px 0;\">"
                + "<strong>授课老师：</strong>" + teacherName + "</p>"
                + "</div>"

                + "<a href=\"" + joinUrl + "\" "
                + "style=\""
                + "display: block;"
                + "width: 200px;"
                + "margin: 20px auto;"
                + "background-color: #2c7be5;"
                + "color: white !important;"
                + "padding: 15px 30px;"
                + "border-radius: 5px;"
                + "text-align: center;"
                + "text-decoration: none;"
                + "font-size: 18px;"
                + "transition: background-color 0.3s;\""
                + "onmouseover=\"this.style.backgroundColor='#1c5bb5'\" "
                + "onmouseout=\"this.style.backgroundColor='#2c7be5'\">"
                + "进入在线课堂"
                + "</a>"

                + "</div>"
                + "</div>";
    }
}