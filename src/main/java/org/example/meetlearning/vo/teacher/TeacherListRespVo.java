package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "role", description = "角色")
    private String role;

    @Schema(name = "name", description = "姓名")
    private String name;

    @Schema(name = "attendance", description = "出席率")
    private BigDecimal attendance;

    @Schema(name = "rating", description = "等级")
    private BigDecimal rating;

    @Schema(name = "price", description = "价格")
    private BigDecimal price;

    @Schema(name = "groupPrice", description = "团队课价格")
    private BigDecimal groupPrice;

    @Schema(name = "coin", description = "课时币")
    private BigDecimal coin;

    @Schema(name = "groupCoin", description = "团队课时币")
    private BigDecimal groupCoin;

    @Schema(name = "absent", description = "旷课数量")
    private BigDecimal absent = BigDecimal.ZERO;

    @Schema(name = "oneStar", description = "一星数量")
    private BigDecimal oneStar = BigDecimal.ZERO;

    @Schema(name = "creditsPrice", description = "积分价格")
    private BigDecimal creditsPrice;

    @Schema(name = "rate", description = "比例")
    private BigDecimal rate;

    @Schema(name = "confirmed", description = "确认数量")
    private BigDecimal confirmed;

    @Schema(name = "canceled", description = "取消数量")
    private BigDecimal canceled;

    @Schema(name = "complaints", description = "投诉数量")
    private BigDecimal complaints;

    @Schema(name = "salary", description = "薪水")
    private BigDecimal salary;

    @Schema(name = "testStatus", description = "测试老师状态")
    private Boolean testStatus;

    @Schema(name = "groupStatus", description = "团体课老师状态")
    private Boolean groupStatus;

    @Schema(name = "managerStatus", description = "管理员状态")
    private Boolean managerStatus;

    @Schema(name = "enableStatus", description = "启用状态")
    private Boolean enableStatus;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "manager", description = "管理者")
    private String manager;

    @Schema(name = "managerId", description = "管理者")
    private String managerId;

}
