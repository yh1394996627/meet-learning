package org.example.meetlearning.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wechat.pay")
@Data
public class WechatPayConfig {
    private String appId;
    private String mchId;
    private String apiKey;
    private String notifyUrl;
    private String tradeType;
    private int httpConnectTimeout;
    private int httpReadTimeout;
}