package org.example.meetlearning.controller.commons;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.zoom.ZoomWebhookPayload;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
@EnableAsync
public class ZoomController implements BaseController {

    private final ZoomPcService zoomPcService;
    private final ZoomOAuthService zoomOAuthService;

    @GetMapping("/zoom/callback")
    public String handleCallback(@RequestParam("code") String authorizationCode) throws IOException {
        return authorizationCode;
    }



    @PostMapping("/api/zoom/event/callback")
    public ResponseEntity<String> handleZoomEvent(
            @RequestHeader(value = "authorization", required = false) String authToken,
            @RequestBody String payload) {
        log.info("payload: {}", payload);
        log.info("authToken: {}", authToken);
        JSONObject json = new JSONObject(payload);
        String eventType = json.getString("event");

        // 处理 URL 验证请求
        if ("endpoint.url_validation".equals(eventType)) {
            return handleUrlValidation(json);
        }

        return ResponseEntity.ok("Event received");
    }


    // 验证Zoom webhook签名的示例方法
    private boolean verifySignature(String signature, String timestamp, ZoomWebhookPayload payload) {
        try {
            String message = "v0:" + timestamp + ":" + payload.toString();
            String secret = "YOUR_ZOOM_WEBHOOK_SECRET";

            Mac sha256 = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            sha256.init(secretKey);

            String hash = "v0=" + Hex.encodeHexString(sha256.doFinal(message.getBytes(StandardCharsets.UTF_8)));

            return MessageDigest.isEqual(hash.getBytes(StandardCharsets.UTF_8), signature.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            return false;
        }
    }


    private ResponseEntity<String> handleUrlValidation(JSONObject json) {
        try {
            String plainToken = json.getJSONObject("payload").getString("plainToken");

            // 使用 HMAC-SHA256 加密令牌
            String encryptedToken = encryptToken(plainToken);

            // 构建响应 JSON
            JSONObject response = new JSONObject();
            response.put("plainToken", plainToken);
            response.put("encryptedToken", encryptedToken);

            log.info("Successfully validated Zoom webhook URL");
            return ResponseEntity.ok(response.toString());

        } catch (Exception e) {
            log.error("URL validation failed", e);
            return ResponseEntity.badRequest().body("Validation failed");
        }
    }

    // 加密令牌方法
    private String encryptToken(String plainToken) throws Exception {
        Mac sha256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(
                "xStegieNSmqcx-E59w8K1A".getBytes(StandardCharsets.UTF_8),
                "HmacSHA256");
        sha256.init(secretKey);

        byte[] hash = sha256.doFinal(plainToken.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(hash);
    }

    // 字节数组转十六进制
    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }


    @GetMapping("/api/zoom/event/callback")
    public String verifyWebhook(@RequestParam("zoom_verification_token") String token) {
        // 返回验证令牌以验证 URL
        log.info("token:{}", token);
        return token;
    }


    @Operation(summary = "判断本地ZOOM是否存在", operationId = "isZoomInstalled")
    @PostMapping(value = "/v1/zoom/installed")
    public RespVo<Boolean> isZoomInstalled() {
        return new RespVo(zoomPcService.isZoomInstalled(getUserCode()));
    }

}