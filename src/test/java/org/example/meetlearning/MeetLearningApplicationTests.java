package org.example.meetlearning;

import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.ZoomPcService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
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
    void test2() throws Exception {
        zoomPcService.getZoomUserId("1394996627@qq.com");
    }




}
