package org.example.meetlearning.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student {

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

    private Integer age;

    private Integer gender;

    private String phone;

    private String email;

    private String country;

    private String lastActivities;

    private String website;

    private String remark;

    private String learnPurpose;

    private String learnPlan;

    private String language;

    private String avatarUrl;

    private String affiliateId;

    private String affiliateName;

    @Transient
    private String encryption;

    @Transient
    private String password;

}