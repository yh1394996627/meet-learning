package org.example.meetlearning.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Service
@Slf4j
public class ZoomOAuthService {

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAccessToken() {
        String url = "https://zoom.us/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Basic " + encodeCredentials(clientId, clientSecret));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
        return response.getBody(); // 返回包含 access_token 的 JSON 字符串
    }

    private String encodeCredentials(String clientId, String clientSecret) {
        String credentials = clientId + ":" + clientSecret;
        String encodeCredentials = java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
        log.info("encodeCredentials:{}", encodeCredentials);
        return java.util.Base64.getEncoder().encodeToString(credentials.getBytes());
    }
}