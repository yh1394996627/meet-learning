package org.example.meetlearning;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;
import java.util.List;

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
    private ZoomOAuthService zoomOAuthService;

    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }

    @Test
    void test2() throws JSONException, IOException {
        String accountId = "E3Ker_gzTQC5z7cOj9goQg";
        String clientId = "136G0saSOqZ5RlwPAT2Vg";
        String clientSecret = "QhWr3zrnirGRutMgwfgPLDxpZWlnO12k";
        String zoomUserId = "QlKYLPwLRgWFOtghx31u8A";
        String accessToken = zoomOAuthService.getValidAccessToken(clientId, clientSecret, accountId);
        String meet = zoomOAuthService.createMeeting(zoomUserId, "test001", "2025-03-30T10:00:00", accessToken);
        // 查看会议信息
        JSONObject meetObj = new JSONObject(meet);
        log.info("meetObj：{}", meetObj);
        //添加会议邮箱自动审批
        zoomOAuthService.addAndApproveParticipants(meetObj.get("id").toString(), List.of("1394996627@qq.com","yuh9527@aliyun.com"), accessToken);

        log.info("123123123");
    }

}
