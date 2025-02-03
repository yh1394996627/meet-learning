package org.example.meetlearning.controller;


import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;


public interface BaseController {

    @JsonIgnore
    default String getUserCode() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("userCode");
    }


    @JsonIgnore
    default String getMacCode() {
        return ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest().getHeader("macCode");
    }

}
