package org.example.meetlearning.vo.config;

import lombok.Data;
import org.example.meetlearning.enums.ConfigTypeEnum;

import java.math.BigDecimal;

@Data
public class BaseConfigReqVo {

    private String recordId;

    private String code;

    private String name;

    private String symbol;

    private ConfigTypeEnum configType;

    private BigDecimal rate;

}
