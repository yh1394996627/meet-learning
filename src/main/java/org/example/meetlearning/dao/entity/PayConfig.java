package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayConfig {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String currencyId;

    private String currencyCode;

    private String currencyName;

    private BigDecimal quantity;

    private BigDecimal amount;

    private Integer days;

}