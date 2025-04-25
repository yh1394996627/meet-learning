package org.example.meetlearning.vo.config;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class BaseConfigRespVo {

    private String recordId;

    private String code;

    private String name;

    private String type;

    private BigDecimal rate;

}
