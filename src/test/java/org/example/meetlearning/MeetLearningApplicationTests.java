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

//    @Autowired
//    private ZoomService zoomService;
//
//    @Autowired
//    private ZoomPcService zoomPcService;
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private ZoomOAuthService zoomOAuthService;
//    @Autowired
//    private TeacherService teacherService;

    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }

    @Test
    void test2() throws JSONException, IOException {
//        String accountId = "E3Ker_gzTQC5z7cOj9goQg";
//        String clientId = "136G0saSOqZ5RlwPAT2Vg";
//        String clientSecret = "QhWr3zrnirGRutMgwfgPLDxpZWlnO12k";
//        String accessToken = zoomOAuthService.getValidAccessToken(clientId, clientSecret, accountId);
//
//        emailPcService.sendReEmail(accessToken, "yuh1394996627@gmail.com");
//        String zoomUserId = zoomOAuthService.getZoomUserIdByEmail(accountId, "yuh9527@aliyun.com", accessToken);
//        String meet = zoomOAuthService.createMeeting11(zoomUserId,"test001", "2025-03-30T10:00:00", CourseTypeEnum.GROUP);
//        // 查看会议信息
//        JSONObject meetObj = new JSONObject(meet);
//        log.info("meetObj：{}", meetObj);
//        //添加会议邮箱自动审批
//        zoomOAuthService.addAndApproveParticipants(meetObj.get("id").toString(), List.of("1394996627@qq.com", "yuh9527@aliyun.com"), accessToken);

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
