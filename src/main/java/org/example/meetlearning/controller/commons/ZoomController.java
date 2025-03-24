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
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Slf4j
@EnableAsync
public class ZoomController {

    private final ZoomPcService zoomPcService;
    private final ZoomService zoomService;

    @GetMapping("/zoom/callback")
    public String handleCallback(@RequestParam("code") String authorizationCode) throws IOException {
        // 使用授权码获取 Access Token
        String accessToken = getAccessToken(authorizationCode);
        // 货取ZOOM用户ID
        String user = zoomService.getZoomUserIdByEmail("1394996627@qq.com", accessToken);
        log.info("user：{}",user);
        JSONObject userObj = new JSONObject(user);
        // 创建会议
        String meet = zoomPcService.createMeeting(userObj.get("id").toString(), "test", "2025-03-23T10:00:00", accessToken);
        log.info("meet：{}",meet);
        // 查看会议信息
        JSONObject meetObj = new JSONObject(meet);
        String meetingInfo =zoomPcService.getMeetingInfo(meetObj.get("uuid").toString(), accessToken);
        log.info("meetingInfo：{}",meetingInfo);
        JSONObject meetingInfoObj = new JSONObject(meetingInfo);
        return "Authorization code: " + authorizationCode + ", Access Token: " + accessToken;
    }


    @PostMapping("/zoom/event/callback")
    public String handleWebhook(@RequestHeader("x-zm-signature") String signature,
                                @RequestHeader("x-zm-request-timestamp") String timestamp,
                                @RequestBody String payload) {
        // 验证签名
        if (!verifySignature(signature, timestamp, payload)) {
            return "Invalid signature";
        }

        // 处理事件
        processEvent(payload);

        return "Event received";
    }

    @GetMapping("/zoom/event/callback")
    public String verifyWebhook(@RequestParam("zoom_verification_token") String token) {
        // 返回验证令牌以验证 URL
        return token;
    }

    private boolean verifySignature(String signature, String timestamp, String payload) {
        // 实现签名验证逻辑
        // 使用 Zoom 提供的密钥和算法验证签名
        return true; // 返回验证结果
    }

    private void processEvent(String payload) {
        // 处理接收到的 Zoom 事件
        // 解析 payload 并根据事件类型执行相应操作
    }

    @Operation(summary = "判断本地ZOOM是否存在", operationId = "isZoomInstalled")
    @PostMapping(value = "v1/zoom/installed")
    public RespVo<Boolean> isZoomInstalled() {
        return zoomPcService.isZoomInstalled();
    }

    private String getAccessToken(String authorizationCode) throws IOException {
        return zoomPcService.getAccessToken(authorizationCode);
    }

}