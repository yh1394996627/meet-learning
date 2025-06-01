package org.example.meetlearning;

import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.TeacherSalaryPcService;
import org.example.meetlearning.service.WechatPayService;
import org.example.meetlearning.service.ZoomPcService;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
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


    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }

    @Test
    void test2() throws JSONException, IOException {
        //todo 会议开始
        String aa = "{\"event\":\"meeting.started\",\"payload\":{\"account_id\":\"E3Ker_gzTQC5z7cOj9goQg\",\"object\":{\"duration\":60,\"start_time\":\"2025-06-01T07:58:52Z\",\"timezone\":\"Asia/Shanghai\",\"topic\":\"08067811-a781-42bc-8bcb-b940ab790cec\",\"id\":\"86222216938\",\"type\":2,\"uuid\":\"ffyZvNmtQpibQgsZVWWygA==\",\"host_id\":\"pYV9SDB3TiKUFchDf007GQ\"}},\"event_ts\":1748764732415}";
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
        teacherSalaryPcService.updateSalary("11111", "2222222", "1adcea54-a93a-4e01-8ebc-d5299df74fa6", new Date());

    }

    @Test
    void test4() throws JSONException, IOException {

    }


    @Test
    void test5() {
        emailPcService.sendNotice("2025-05-19 10:30", "Join", "https://www.12talk.com", "1394996627@qq.com");
    }
}
