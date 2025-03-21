package org.example.meetlearning.controller.commons;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@AllArgsConstructor
@Slf4j
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


    @PostMapping("/zoom/pur")
    public String handleWebhook(@RequestBody String payload) {
        try {
            // 解析 Webhook 数据
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(payload);

            // 获取事件类型
            String eventType = jsonNode.path("event").asText();

            // 处理会议结束事件
            if ("meeting.ended".equals(eventType)) {
                JsonNode payloadNode = jsonNode.path("payload");
                String meetingId = payloadNode.path("object").path("id").asText();
                String endTime = payloadNode.path("object").path("end_time").asText();

                // 处理会议结束逻辑
                System.out.println("会议结束通知：");
                System.out.println("会议 ID: " + meetingId);
                System.out.println("结束时间: " + endTime);
            }
            return "Webhook received";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error processing webhook";
        }
    }

    private String getAccessToken(String authorizationCode) throws IOException {
        return zoomPcService.getAccessToken(authorizationCode);
    }

}