package org.example.meetlearning.service;

import com.aliyun.dm20151123.Client;
import com.aliyun.dm20151123.models.SingleSendMailRequest;
import com.aliyun.dm20151123.models.SingleSendMailResponse;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailPcService {

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

    public RespVo<String> sendVerificationEmail(String toEmail) throws Exception {
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
            return new RespVo<>("发送成功");
        } catch (Exception e) {
            log.error("发送失败", e);
            return new RespVo<>("发送失败", false, e.getMessage());
        }
    }
}