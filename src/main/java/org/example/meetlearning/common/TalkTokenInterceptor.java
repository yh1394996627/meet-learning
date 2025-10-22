package org.example.meetlearning.common;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Slf4j
public class TalkTokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 排除非控制器方法
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        // 获取 talkToken
        String talkToken = request.getHeader("talkToken");
        String userCode = request.getHeader("userCode");
        log.info("talkToken: {}", talkToken);
        if (StringUtils.isEmpty(talkToken)) {
            throw new BusinessException(401, "talk token expired");
        }
        User user = userService.selectByRecordId(userCode);
        log.info("base---->user: {}", user);
        if (user != null) {
            Long pwVersion = user.getPwVersion();
            pwVersion = pwVersion == null ? 1 : pwVersion;
            log.info("pwVersion: {},talkToken:{}", pwVersion, talkToken);
            if (!StringUtils.equals(talkToken, pwVersion.toString())) {
                throw new BusinessException(401, "talk token expired");
            }
        }
        return true;
    }
}