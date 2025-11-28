package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Teacher {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String name;

    private String enName;

    private Integer gender;

    private BigDecimal attendance;

    private BigDecimal rating;

    private BigDecimal price;

    private BigDecimal groupPrice;

    private BigDecimal creditsPrice;

    private BigDecimal coin;

    private BigDecimal groupCoin;

    private BigDecimal rate;

    private BigDecimal confirmedQty;

    private BigDecimal canceledQty;

    private BigDecimal complaintsQty;

    private BigDecimal salaryAmount;

    private String email;

    private String country;

    private String managerId;

    private String manager;

    private String avatarUrl;

    private String language;

    private String currencyCode;

    private String currencyName;

    private String currencySymbol;

    private String specialties;

    private Boolean managerStatus;

    private Boolean testStatus;

    private Boolean enabledStatus;

    private String videoUrl;

    private String zoomUserId;

    private String zoomAccountId;

    private Boolean zoomActivationStatus;

    private Boolean groupStatus;

    private String meetLink;

    private String meetPassWord;


}