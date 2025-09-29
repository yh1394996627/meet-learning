package org.example.meetlearning.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TalkTokenInterceptor talkTokenInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(talkTokenInterceptor)
                .addPathPatterns(
                        "/v1/affiliate/**",     // 学生课程接口
                        "/v1/config/**",    // 老师课程接口
                        "/v1/manager/**",            // 管理接口
                        "/v1/student/**",             // 用户相关接口
                        "/v1/schedule//**",             // 用户相关接口
                        "/v1/textbook/**",             // 用户相关接口
                        "/v1/zoom/**"           // 其他安全接口
                ) // 拦截所有学生课程接口
                .excludePathPatterns("/swagger**", "/webjars/**", "/v3/api-docs**"); // 排除 Swagger 文档
    }
}