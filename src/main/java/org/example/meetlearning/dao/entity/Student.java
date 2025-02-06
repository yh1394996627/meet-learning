package org.example.meetlearning.dao.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    private Integer age;

    private Integer gender;

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