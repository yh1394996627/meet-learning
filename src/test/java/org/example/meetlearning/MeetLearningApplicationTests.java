package org.example.meetlearning;

import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.ZoomPcService;
import org.example.meetlearning.service.ZoomService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

@SpringBootTest
@Slf4j
class MeetLearningApplicationTests {

    @Autowired
    private EmailPcService emailPcService;


    @Autowired
    private ZoomPcService zoomPcService;


    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }

    @Test
    void test2() throws JSONException, IOException {
        String aa = "{\"event\":\"meeting.started\",\"payload\":{\"account_id\":\"E3Ker_gzTQC5z7cOj9goQg\",\"object\":{\"duration\":60,\"start_time\":\"2025-05-23T08:35:21Z\",\"timezone\":\"Asia/Shanghai\",\"topic\":\"e48d2eb5-7ee6-4134-9577-d461ab617ac8\",\"id\":\"88685309085\",\"type\":2,\"uuid\":\"0axSNQv0Try0LVCwJiBpvQ==\",\"host_id\":\"pYV9SDB3TiKUFchDf007GQ\"}},\"event_ts\":1747989321374}";
        //开始会议 - 加入会议
        zoomPcService.handleZoomEvent(null,aa);
        log.info("123123123");
    }

    @Test
    void test3() throws JSONException, IOException {
//        String accountId = "E3Ker_gzTQC5z7cOj9goQg";
//        String clientId = "136G0saSOqZ5RlwPAT2Vg";
//        String clientSecret = "QhWr3zrnirGRutMgwfgPLDxpZWlnO12k";
//        String email = "yuh9527@aliyun.com";
//        zoomOAuthService.getValidAccessToken(clientId, clientSecret, accountId);
//        JsonObject user = zoomOAuthService.getUserInfo(email);
//        Teacher teacher = teacherService.selectByRecordId("7dcff5f9-1c55-4035-b4dc-1fb4954e4a29");
//        teacher.setZoomAccountId(accountId);
//        zoomOAuthService.createMeeting(teacher, "test001", "2025-04-02T18:00:00", CourseTypeEnum.GROUP);

    }

    @Test
    void test4() throws JSONException, IOException {
//        String accountId = "E3Ker_gzTQC5z7cOj9goQg";
//        String clientId = "136G0saSOqZ5RlwPAT2Vg";
//        String clientSecret = "QhWr3zrnirGRutMgwfgPLDxpZWlnO12k";
//        String email = "yuh1934996627@gmail.com";
//        String accessToken = zoomOAuthService.getValidAccessToken(clientId, clientSecret, accountId);
//        String id = zoomOAuthService.getUserIdByEmail(email, accessToken);

    }


    @Test
    void test5() {
        emailPcService.sendNotice("2025-05-19 10:30", "Join", "https://www.12talk.com", "1394996627@qq.com");
    }
}
