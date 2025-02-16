package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Affiliate {

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

    private String email;

    private BigDecimal balance;

    private BigDecimal commissionRate;

    private BigDecimal rate;

    private BigDecimal studentTotal;

    private BigDecimal courseTotal;

    private String remark;

    private String recommenderId;

    private String recommender;

}