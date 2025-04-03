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
    public ResponseEntity<String> handleWebhookEvent(
            @RequestBody ZoomWebhookPayload payload,
            @RequestHeader("x-zm-signature") String signature,
            @RequestHeader("x-zm-request-timestamp") String timestamp) {

        // 1. 验证签名(可选但推荐)
        // if (!verifySignature(signature, timestamp, payload)) {
        //     return ResponseEntity.status(401).build();
        // }

        // 2. 处理不同的事件类型
        switch (payload.getEvent()) {
            case "meeting.started":
                zoomOAuthService.handleMeetingStarted(payload);
                break;
            case "meeting.ended":
                zoomOAuthService.handleMeetingEnded(payload);
                break;
            // 可以处理其他事件...
            default:
                break;
        }

        return ResponseEntity.ok().build();
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