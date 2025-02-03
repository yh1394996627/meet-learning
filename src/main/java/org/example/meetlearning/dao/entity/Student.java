package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class Student {

    private Integer id;

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

    private String encryption;

    private String password;

    private Long age;

    private Boolean gender;

    private String phone;

    private String email;

    private BigDecimal balance;

    private BigDecimal consumption;

    private Date expirationTime;

    private String lastActivities;

    private String website;

    private String recommender_id;

    private String recommender;

    private String remark;

    private String learnPurpose;

    private String learnPlan;

    private String language;

}