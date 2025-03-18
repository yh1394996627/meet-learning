package org.example.meetlearning.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class ZoomAuthUtil {

    public static String generateJWT(String API_KEY, String API_SECRET) {
        // 将 Base64 编码的 API Secret 解码为字节数组
        byte[] secretBytes = Base64.getDecoder().decode("Vo8mX3QgFtfOMU1hWlobgKh8KNQhXyJX");

        return Jwts.builder()
                .setIssuer(API_KEY)
                .setExpiration(new Date(System.currentTimeMillis() + 3600 * 1000)) // 1 小时有效期
                .signWith(Keys.hmacShaKeyFor(secretBytes), SignatureAlgorithm.HS256) // 使用解码后的密钥
                .compact();
    }


    public static String createMeeting(String zoomUserId, String topic, Date startTime, int duration, String ZOOM_API_URL, String jwt) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("topic", topic);
        requestBody.put("type", 2); // 2 表示预定会议
        requestBody.put("start_time", new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").format(startTime));
        requestBody.put("duration", duration);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Map> response = restTemplate.postForEntity(
                ZOOM_API_URL + "/users/" + zoomUserId + "/meetings", request, Map.class);
        return (String) response.getBody().get("join_url");
    }

}