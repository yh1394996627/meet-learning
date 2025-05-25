package org.example.meetlearning;

import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.WechatPayService;
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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
@Slf4j
class MeetLearningApplicationTests {

    @Autowired
    private EmailPcService emailPcService;


    @Autowired
    private ZoomPcService zoomPcService;

    @Autowired
    private WechatPayService wechatPayService;


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
    void test7() throws Exception {
        String aa = "{transaction_id=4200002721202505257642769585, nonce_str=f8cdbb80ca014d9aab64758d277148b8, bank_type=OTHERS, openid=opqzC6mF93a7Qzps_rOkHjHneBJw, sign=71EDAF4751E0D66528498CF1C8CF9E9D, fee_type=CNY, mch_id=1716062481, cash_fee=100, out_trade_no=c1603593cce144cab388b8d5a1f42e63, appid=wx8b23a9b431490cbf, total_fee=100, trade_type=NATIVE, result_code=SUCCESS, time_end=20250525144752, is_subscribe=N, return_code=SUCCESS}";
        Map<String, String> map = new HashMap<>();
        Pattern pattern = Pattern.compile("(\\w+)=([^,}]+)");
        Matcher matcher = pattern.matcher(aa);

        while (matcher.find()) {
            String key = matcher.group(1).trim();
            String value = matcher.group(2).trim();
            map.put(key, value);
        }
        //开始会议 - 加入会议
        wechatPayService.paymentNotify111(map);
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
