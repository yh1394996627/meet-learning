package org.example.meetlearning;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan("org.example.meetlearning.dao.mapper")
public class MeetLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetLearningApplication.class, args);
    }

}
