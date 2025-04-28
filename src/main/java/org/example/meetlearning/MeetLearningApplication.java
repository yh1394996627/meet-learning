package org.example.meetlearning;

import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.databind.util.ISO8601DateFormat;
import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.TimeZone;

@SpringBootApplication
@MapperScan("org.example.meetlearning.dao.mapper")
@EnableScheduling
@EnableTransactionManagement
//                      @EnableAsync
public class MeetLearningApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeetLearningApplication.class, args);
    }


    // 在启动类或配置类中添加
    @PostConstruct
    void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    // 配置Jackson全局序列化
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
        return builder -> {
            builder.simpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            builder.serializers(new DateSerializer(false, new ISO8601DateFormat()));
        };
    }
}
