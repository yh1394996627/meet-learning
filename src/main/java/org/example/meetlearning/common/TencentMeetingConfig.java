package org.example.meetlearning.common;

import com.tencentcloudapi.wemeet.Client;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "voov.meeting")
@Data
public class TencentMeetingConfig {
    private String appId;
    private String entId;
    private String secretId;
    private String secretKey;
    private String adminUserId;
    private String adminAccountType;


    public Client getClient() {
        return new Client.Builder().withAppId(appId).withSdkId(entId).withSecret(secretId, secretKey).build();
    }
}