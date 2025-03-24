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

    @Schema(name = "managerStatus", description = "管理员状态")
    private Boolean managerStatus;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "manager", description = "管理者")
    private String manager;

    @Schema(name = "managerId", description = "管理者")
    private String managerId;

}
