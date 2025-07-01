package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.util.BigDecimalUtil;

import java.math.BigDecimal;

@Data
public class TeacherDashboardRespVo {

    @Schema(name = "rate", description = "比率")
    private BigDecimal rate;
    @Schema(name = "confirmedQty", description = "已确认课程数量")
    private BigDecimal confirmedQty;
    @Schema(name = "cancelledQty", description = "已取消课程数量")
    private BigDecimal cancelledQty;
    @Schema(name = "complaintsQty", description = "投诉数量")
    private BigDecimal complaintsQty;
    @Schema(name = "absentQty", description = "缺席数量")
    private BigDecimal absentQty;
    @Schema(name = "confirmedClassesAmount", description = "已确认课程金额")
    private BigDecimal confirmedClassesAmount;
    @Schema(name = "cancelledDeductionsAmount", description = "已取消课程扣款金额")
    private BigDecimal cancelledDeductionsAmount;
    @Schema(name = "complaintDeductionsAmount", description = "投诉扣款金额")
    private BigDecimal complaintDeductionsAmount;
    @Schema(name = "absentAmount", description = "缺席扣款金额")
    private BigDecimal absentAmount;
    @Schema(name = "bonus", description = "老师奖金")
    private BigDecimal bonus;
    @Schema(name = "commission", description = "老师佣金")
    private BigDecimal commission;
    @Schema(name = "totalSalary", description = "老师工资")
    private BigDecimal totalSalary;
    @Schema(name = "attendanceRate", description = "老师出勤率")
    private BigDecimal attendanceRate;
    @Schema(name = "rating", description = "老师评分")
    private BigDecimal rating;
    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;
    @Schema(name = "currencyName", description = "币种")
    private String currencyName;


    @Schema(hidden = true)
    public BigDecimal getTotalSalary() {
        return confirmedClassesAmount.subtract(BigDecimalUtil.nullOrZero(cancelledDeductionsAmount))
                .subtract(BigDecimalUtil.nullOrZero(absentAmount))
                .subtract(BigDecimalUtil.nullOrZero(complaintDeductionsAmount))
                .add(BigDecimalUtil.nullOrZero(bonus))
                .add(BigDecimalUtil.nullOrZero(commission));
    }


}
