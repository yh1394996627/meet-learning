package org.example.meetlearning.service;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.apache.ibatis.annotations.Param;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.util.SystemUUIDUtil;
import org.example.meetlearning.util.ZoomDetectorUtil;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

@Service
@Slf4j
public class ZoomPcService {

    @Value("${zoom.client-id}")
    private String apiKey;

    @Value("${zoom.client-secret}")
    private String apiSecret;

    @Value("${zoom.api-url}")
    private String apiUrl;

    @Autowired
    private RedisTemplate redisTemplate;

    private OkHttpClient client = new OkHttpClient();

    public String getZoomUserId(String email, String accessToken) throws IOException {
        String url = apiUrl + "/users/" + email;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string(); // Parse the response to get the userId
        }
    }

    public String createMeeting(String userId, String topic, String startTime, String accessToken) throws IOException {
        String url = apiUrl + "/users/" + userId + "/meetings";
        String json = "{\"topic\":\"" + topic + "\",\"start_time\":\"" + startTime + "\"}";
        RequestBody body = RequestBody.create(json, MediaType.parse("application/json"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string(); // Parse the response to get the meeting details
        }
    }

    public String getMeetingInfo(String meetingId, String accessToken) throws IOException {
        String url = apiUrl + "/meetings/" + meetingId;
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + accessToken)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ", body: " + response.body().string());
            }
            return response.body().string();
        }
    }

    public String getAccessToken() throws IOException {
        String url = "https://zoom.us/oauth/token";
        String credentials = Credentials.basic(apiKey, apiSecret);
        RequestBody body = RequestBody.create("grant_type=client_credentials", MediaType.parse("application/x-www-form-urlencoded"));
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(body)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            return response.body().string(); // Parse the response to get the access token
        }
    }


    public String getAccessToken(String authorizationCode) throws IOException {
        String url = "https://zoom.us/oauth/token";
        String credentials = Credentials.basic(apiKey, apiSecret);

        // 构建请求体
        RequestBody body = new FormBody.Builder()
                .add("grant_type", "authorization_code")
                .add("code", authorizationCode)
                .add("redirect_uri", "https://localhost:5001/zoom/callback")
                .build();

        // 构建请求
        Request request = new Request.Builder()
                .url(url)
                .addHeader("Authorization", credentials)
                .post(body)
                .build();

        // 发送请求
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response + ", body: " + response.body().string());
            }
            // 解析响应体，获取 Access Token
            String responseBody = response.body().string();
            return new ObjectMapper().readTree(responseBody).get("access_token").asText();
        }
    }
}
