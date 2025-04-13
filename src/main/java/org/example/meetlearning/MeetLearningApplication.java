package org.example.meetlearning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("org.example.meetlearning.dao.mapper")
@EnableScheduling
@EnableTransactionManagement
@EnableAsync
public class MeetLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetLearningApplication.class, args);
    }

}
