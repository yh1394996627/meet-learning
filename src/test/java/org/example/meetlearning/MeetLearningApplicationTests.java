package org.example.meetlearning;

import org.example.meetlearning.service.EmailPcService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MeetLearningApplicationTests {

    @Autowired
    private EmailPcService emailPcService;

    @Test
    void contextLoads() throws Exception {
        emailPcService.sendVerificationEmail("1394996627@qq.com");
    }

}
