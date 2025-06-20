package org.example.meetlearning.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


public interface BaseHandler {

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






}
