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
    private String creditsPrice;

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





}
