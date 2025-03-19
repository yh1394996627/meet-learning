package org.example.meetlearning.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtil {

    public static String generateJWT(String apiKey, String apiSecret) {
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer(apiKey)
                .setIssuedAt(now)
                .setExpiration(new Date(nowMillis + 3600 * 1000)) // 1小时有效期
                .signWith(SignatureAlgorithm.HS256, apiSecret.getBytes())
                .compact();
    }
}