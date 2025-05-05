package org.example.meetlearning.util;

import com.alibaba.fastjson2.JSON;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class TokenSign {
    public static String sign(LoginUser loginUser, String encryptKey) {
        String userInfo = JSON.toJSONString(loginUser);
        String signature = Hmac.getInstance().getSHA1Base64(userInfo, encryptKey);
        // 使用 Java 内置 Base64
        String base64UserInfo = Base64.getEncoder().encodeToString(userInfo.getBytes(StandardCharsets.UTF_8));
        String token = base64UserInfo + "." + signature;
        // 使用 Java 内置 URLEncoder
        try {
            return URLEncoder.encode(token, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("URL encode failed", e);
        }
    }

    public static void main(String[] args) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(46L);
        loginUser.setTenantId("9def9391-9d24-4488-867e-594cfc35d254");
        loginUser.setCategory(2);
        String encryptKey = "7xVtRkGpLq9Yfz2wM8nJcB4hDmK3Td6AeXqP1oW0ySvF5U=";
        String token = sign(loginUser,encryptKey );
        System.out.println("token->"+token);
    }


}