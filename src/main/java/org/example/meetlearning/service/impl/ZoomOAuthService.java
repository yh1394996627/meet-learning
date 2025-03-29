package org.example.meetlearning.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.util.RedisCommonsUtil;
import org.example.meetlearning.vo.zoom.ZoomAccountInfoVo;
import org.example.meetlearning.vo.zoom.ZoomBaseVerifyRespVo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class ZoomOAuthService {

    private String accessToken;

    private final RestTemplate restTemplate = new RestTemplate();

    @Autowired
    private RedisTemplate redisTemplate;

    public String getValidAccessToken(String clientId, String clientSecret, String accountId) {
        Object redis = redisTemplate.opsForValue().get(clientId + clientSecret + accountId);
        if (redis == null) {
            refreshToken(clientId, clientSecret, accountId);
        } else {
            accessToken = redis.toString();
        }
        return accessToken;
    }

    private synchronized void refreshToken(String clientId, String clientSecret, String accountId) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String auth = clientId + ":" + clientSecret;
            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());
            headers.set("Authorization", "Basic " + encodedAuth);

            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("grant_type", "account_credentials");
            body.add("account_id", accountId);

            ResponseEntity<Map> response = new RestTemplate().exchange(
                    "https://zoom.us/oauth/token",
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class);

            this.accessToken = (String) Objects.requireNonNull(response.getBody()).get("access_token");
            int expiresIn = (Integer) response.getBody().get("expires_in");
            redisTemplate.opsForValue().set(clientId + clientSecret + accountId, accessToken, 3590, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh Zoom access token", e);
        }
    }


    //token校验
    public ZoomBaseVerifyRespVo isTokenValid(String accessToken) {
        String url = "https://api.zoom.us/v2/users/me";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        try {
            ResponseEntity<String> response = new RestTemplate().exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    String.class
            );
            return new ZoomBaseVerifyRespVo(response.getStatusCode() == HttpStatus.OK, response.getStatusCode().value(), response.getBody(), null);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) { // 401 表示无效
                new ZoomBaseVerifyRespVo(false, HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase(), null);
            }
            new ZoomBaseVerifyRespVo(false, e.getStatusCode().value(), e.getStatusCode().toString(), null);
            throw e;
        }
    }

    public String getZoomUserIdByEmail(String email, String accessToken) {
        String url = "https://api.zoom.us/v2/users/" + email;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        log.info("user：{}",  response.getBody());
        JSONObject userObj = new JSONObject( response.getBody());
        return userObj.getString("id");
    }


    public String getAccountPlanType(String accountId, String accessToken) {
        String url = "https://api.zoom.us/v2/users/accounts/" + accountId;

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        ResponseEntity<Object> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                Object.class
        );


        return Objects.requireNonNull(response.getBody()).toString();
    }
}