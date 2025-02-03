package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentUpdateReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "name", description = "姓名")
    private String name;

    @Schema(name = "age", description = "年龄")
    private BigDecimal age;

    @Schema(name = "gender", description = "性别 0男  1女")
    private Integer gender;

    @Schema(name = "learnPurpose", description = "学习目的")
    private String learnPurpose;

    @Schema(name = "learnPlan", description = "学习计划")
    private String learnPlan;

    @Schema(name = "recommender", description = "推荐人")
    private String recommender;




}
