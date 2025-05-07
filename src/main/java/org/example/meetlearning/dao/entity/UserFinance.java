package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserFinance {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String userId;

    private BigDecimal balanceQty;

    private BigDecimal creditsBalance;

    private BigDecimal consumptionQty;

    private BigDecimal amount;

    private String userType;

    private Date expirationTime;

    private Long version;
}