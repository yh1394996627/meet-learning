package org.example.meetlearning.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.common.BusinessException;
import org.example.meetlearning.common.SpringContextHolder;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.service.impl.UserService;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


public interface BaseHandler {


    @JsonIgnore
    default UserService getUserService() {
        return SpringContextHolder.getBean(UserService.class);
    }

    @JsonIgnore
    default String getUserCode() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("userCode");
    }

    @JsonIgnore
    default String getUserName() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("userName");
    }

    @JsonIgnore
    default String getLanguage() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("language");
    }

    @JsonIgnore
    default String getTalkToken() {
        String talkToken = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("talkToken");
        Assert.isTrue(StringUtils.isNotEmpty(talkToken), "talk token is empty");
        if (StringUtils.isEmpty(talkToken)) {
            throw new BusinessException(401, "talk token expired");
        }
        User user = getUserService().selectByRecordId(getUserCode());
        if (user != null) {
            Long pwVersion = user.getPwVersion();
            pwVersion = pwVersion == null ? 1 : pwVersion;
            if (!StringUtils.equals(talkToken, pwVersion.toString())) {
                throw new BusinessException(401, "talk token expired");
            }
        }
        return talkToken;
    }
}
