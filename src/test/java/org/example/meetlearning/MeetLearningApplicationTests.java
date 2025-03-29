package org.example.meetlearning;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.ZoomOAuthServiceTest;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.Objects;

@SpringBootTest
@Slf4j
class MeetLearningApplicationTests {

    @Autowired
    private EmailPcService emailPcService;

    @Autowired
    private ZoomService zoomService;

    @Autowired
    private ZoomPcService zoomPcService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ZoomOAuthServiceTest zoomOAuthServiceTest;

    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }


    @Test
    void test2() throws JSONException, IOException {
        String accessToken = zoomOAuthServiceTest.getValidAccessToken();
        if(StringUtils.isEmpty(accessToken)) {
            accessToken = zoomPcService.getAccessToken();
        }
        // 获取ZOOM用户ID
        String user = zoomService.getZoomUserIdByEmail("1394996627@qq.com", accessToken);
        JSONObject userObj = new JSONObject(user);
        log.info("userObj：{}", userObj);
        // 创建会议
        String meet = zoomPcService.createMeeting(userObj.get("id").toString(), "test", "2025-03-29T10:00:00", accessToken);
        // 查看会议信息
        JSONObject meetObj = new JSONObject(meet);
        log.info("meetObj：{}", meetObj);
        String meetingInfo = zoomPcService.getMeetingInfo(meetObj.get("id").toString(), accessToken);
        JSONObject meetingInfoObj = new JSONObject(meetingInfo);
        log.info("meetingInfoObj：{}", meetingInfoObj);
    }

}
