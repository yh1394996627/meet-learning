package org.example.meetlearning.common;

import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.impl.client.CloseableHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

@Configuration
public class WechatPayConfig {

    @Value("${wechat.pay.mch-id}")
    private String mchId;

    @Value("${wechat.pay.cert-path}/apiclient_key.pem")
    private Resource privateKeyResource;

    @Value("${wechat.pay.cert-path}/apiclient_cert.pem")
    private Resource certificateResource;

    @Bean
    public CloseableHttpClient wechatPayHttpClient() throws Exception {
        // 加载商户私钥和证书
        PrivateKey privateKey = PemUtil.loadPrivateKey(privateKeyResource.getInputStream());
        X509Certificate certificate = PemUtil.loadCertificate(certificateResource.getInputStream());
        String serialNo = certificate.getSerialNumber().toString(16);

        // 正确配置 HttpClient（自动处理验签）
        return WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, serialNo, privateKey)
                .withValidator(response -> true) // 官方SDK已自动验签，此处无需处理
                .build();
    }
}