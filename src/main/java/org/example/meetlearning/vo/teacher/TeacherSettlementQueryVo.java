package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.meetlearning.vo.common.RecordIdQueryVo;

import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
public class TeacherSettlementQueryVo extends RecordIdQueryVo {

    @Schema(name = "beginDate", description = "统计开始日期 yyyy-MM-dd(预览用)")
    private String beginDate;

    @Schema(name = "endDate", description = "统计结束日期 yyyy-MM-dd(预览用)")
    private String endDate;

    @Schema(name = "totalSalary", description = "最终结算工资(可编辑,为空则按计算值)")
    private BigDecimal totalSalary;

}
