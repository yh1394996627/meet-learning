package org.example.meetlearning.vo.teacher;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @Schema(description = "结算日期")
    private Date settlementDate;

    @Schema(description = "是否结算")
    private Boolean isVerification;
}
