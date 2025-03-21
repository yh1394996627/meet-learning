package org.example.meetlearning;

import org.example.meetlearning.service.EmailPcService;
import org.example.meetlearning.service.ZoomService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeetLearningApplicationTests {

    @Autowired
    private EmailPcService emailPcService;


    @Autowired
    private ZoomService zoomService;

    @Test
    void test1() throws Exception {
        emailPcService.sendVerificationEmail("", "1394996627@qq.com ");
    }


    @Test
    void test2(){
        zoomService.getZoomUserIdByEmail("1394996627@qq.com","");
    }




}
