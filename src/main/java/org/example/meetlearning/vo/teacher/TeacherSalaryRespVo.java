package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherSalaryRespVo {

    @Schema(description = "记录ID")
    private String recordId;

    @Schema(description = "教师ID")
    private String teacherId;

    @Schema(description = "薪资")
    private BigDecimal salary;

    @Schema(description = "币种")
    private String currencyCode;

    @Schema(description = "结算日期")
    private Date settlementDate;
}
