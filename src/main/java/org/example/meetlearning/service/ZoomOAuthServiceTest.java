package org.example.meetlearning.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
public class ZoomOAuthServiceTest {

    @Value("${zoom.client-id}")
    private String clientId;

    @Value("${zoom.client-secret}")
    private String clientSecret;

    @Value("${zoom.account-id}")
    private String accountId;

    private String accessToken;
    private Instant tokenExpiry;

    public String getValidAccessToken() {
        if (accessToken == null || Instant.now().isAfter(tokenExpiry.minusSeconds(300))) {
            refreshToken();
        }
        return accessToken;
    }

    private synchronized void refreshToken() {
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
            this.tokenExpiry = Instant.now().plusSeconds(expiresIn);

        } catch (Exception e) {
            throw new RuntimeException("Failed to refresh Zoom access token", e);
        }
    }


//    public User getUser(String email) {
//        String url = "https://api.zoom.us/v2/users/" + email;
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(getValidAccessToken());
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        try {
//            ResponseEntity<User> response = restTemplate.exchange(
//                    url,
//                    HttpMethod.GET,
//                    new HttpEntity<>(headers),
//                    User.class);
//
//            return response.getBody();
//        } catch (HttpClientErrorException e) {
//            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
//                // 令牌可能已过期，强制刷新并重试一次
//                zoomOAuthService.refreshToken();
//                headers.setBearerAuth(zoomOAuthService.getValidAccessToken());
//
//                ResponseEntity<User> retryResponse = restTemplate.exchange(
//                        url,
//                        HttpMethod.GET,
//                        new HttpEntity<>(headers),
//                        User.class);
//
//                return retryResponse.getBody();
//            }
//            throw e;
//        }
//    }
}