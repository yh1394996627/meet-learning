package org.example.meetlearning.controller.commons;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.example.meetlearning.vo.common.RespVo;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Objects;

@RestController
@AllArgsConstructor
@Slf4j
@EnableAsync
public class ZoomController {

    private final ZoomPcService zoomPcService;
    private final ZoomService zoomService;
    private final RedisTemplate redisTemplate;

    @GetMapping("/zoom/callback")
    public void handleCallback(@RequestParam("code") String authorizationCode) throws IOException {
        meeting(authorizationCode);
    }

    private void meeting(String authorizationCode) throws IOException {
        // 使用授权码获取 Access Token
        String accessToken = zoomPcService.getAccessToken(authorizationCode);
        redisTemplate.opsForValue().set("zoom_access_token", accessToken);

        // 获取ZOOM用户ID
        String user = zoomService.getZoomUserIdByEmail("1394996627@qq.com", accessToken);
        log.info("user：{}", user);
        JSONObject userObj = new JSONObject(user);
        // 创建会议
        String meet = zoomPcService.createMeeting(userObj.get("id").toString(), "test", "2025-03-29T10:00:00", accessToken);
        log.info("meet：{}", meet);
        // 查看会议信息
        JSONObject meetObj = new JSONObject(meet);
        String meetingInfo = zoomPcService.getMeetingInfo(meetObj.get("id").toString(), accessToken);
        log.info("meetingInfo：{}", meetingInfo);
        JSONObject meetingInfoObj = new JSONObject(meetingInfo);
    }


    @PostMapping("/zoom/event/callback")
    public String handleWebhook(@RequestHeader("x-zm-signature") String signature,
                                @RequestHeader("x-zm-request-timestamp") String timestamp,
                                @RequestBody String payload) {
        return "Event received";
    }

    @Operation(summary = "判断本地ZOOM是否存在", operationId = "isZoomInstalled")
    @PostMapping(value = "v1/zoom/installed")
    public RespVo<Boolean> isZoomInstalled() {
        return new RespVo(true);
    }


    @GetMapping("/zoom/event/callback")
    public String verifyWebhook(@RequestParam("zoom_verification_token") String token) {
        // 返回验证令牌以验证 URL
        return token;
    }
}