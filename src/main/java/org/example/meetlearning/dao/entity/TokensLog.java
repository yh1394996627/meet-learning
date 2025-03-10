package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TokensLog {

    private Long id;

    private Boolean deleted;

    private String userId;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private BigDecimal quantity;

    private BigDecimal balance;

    private BigDecimal amount;

    private String currencyCode;

    private String currencyName;

    private String currencySymbol;

    private String note;

}