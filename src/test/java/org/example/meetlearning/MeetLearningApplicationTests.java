package org.example.meetlearning;

import cn.hutool.core.date.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.service.*;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
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

    @Autowired
    private TeacherSalaryPcService teacherSalaryPcService;

    @Autowired
    private ZoomOAuthService zoomOAuthService;

    @Autowired
    private TeacherService teacherService;

    @Autowired
    private TaskPcService taskPcService;


    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }

    @Test
    void test2() throws JSONException, IOException {
        //todo 会议开始
        String aa = "{\"payload\":{\"account_id\":\"rq8TEA_HRDCg2mPI6JZ0IQ\",\"object\":{\"uuid\":\"J6baDZUmSNi5eN2ODUSFFA==\",\"participant\":{\"public_ip\":\"223.73.64.72\",\"user_id\":\"16778240\",\"user_name\":\"Yuhang yuh\",\"participant_user_id\":\"qzi0MfIpQ-KXzzOKxPCMCw\",\"id\":\"pYV9SDB3TiKUFchDf007GQ\",\"join_time\":\"2025-06-17T07:55:24Z\",\"email\":\"yuh1394996627@gmail.com\",\"participant_uuid\":\"19F194AA-35F7-6457-9374-177DC0EC551C\"},\"id\":\"83494518167\",\"type\":2,\"topic\":\"3310f7e3-693e-4afe-956f-569ec81c4fd0\",\"host_id\":\"pYV9SDB3TiKUFchDf007GQ\",\"duration\":60,\"start_time\":\"2025-06-17T07:55:24Z\",\"timezone\":\"Asia/Shanghai\"}},\"event_ts\":1750146925910,\"event\":\"meeting.participant_joined\"}";
        //开始会议 - 加入会议
        zoomPcService.handleZoomEvent(null, aa);
        log.info("123123123");
    }

    @Test
    void test7() throws Exception {
        //todo 支付单元测试
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
        //todo 薪资结算测试接口
        teacherSalaryPcService.updateSalary("SYSTEM", "SYSTEM", "8ec4a6b3-e1c2-4871-bf64-8f4fe390775a", new Date());

    }

    @Test
    void test4() throws JSONException, IOException, ParseException {
        Teacher teacher = teacherService.selectByRecordId("1adcea54-a93a-4e01-8ebc-d5299df74fa6");

        zoomOAuthService.createMeeting(teacher,  "1adcea54-a93a-4e01-8ebc-d5299df74fa6", DateUtil.format(new Date(), "yyyy-MM-dd HH:mm"), CourseTypeEnum.SINGLE);
    }


    @Test
    void test5() {
        emailPcService.sendNotice("2025-05-19 10:30", "Join", "https://www.12talk.com", "1394996627@qq.com");
    }

    @Test
    void test6() {
        taskPcService.runTaskAtMidnight();
    }
}
