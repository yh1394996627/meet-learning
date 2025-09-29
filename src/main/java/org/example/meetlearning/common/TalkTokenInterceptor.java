package org.example.meetlearning.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class TalkTokenInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 排除非控制器方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        // 获取 talkToken
        String talkToken = request.getHeader("talkToken");
        if (StringUtils.isEmpty(talkToken)) {
            throw new BusinessException(401, "talk token expired");
        }
        return true;
    }
}