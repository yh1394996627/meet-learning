package org.example.meetlearning.vo.student;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentInfoRespVo {

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

    private BigDecimal balance;

    private BigDecimal consumption;

    private String country;

    private Date expirationTime;

    private String lastActivities;

    private String website;

    private String recommender_id;

    private String recommender;

    private String remark;

    private String learnPurpose;

    private String learnPlan;

    private String language;

    private String affiliateId;

    private String affiliateName;

    @Schema(name = "avatarUrl", description = "头像")
    private String avatarUrl;

}
