package org.example.meetlearning.common;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "voov.meeting")
@Data
public class VoovMeetingConfig {
    private String appId;
    private String secretId;
    private String secretKey;
    private String baseUrl;
}