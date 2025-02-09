package org.example.meetlearning.vo.classes;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class StudentClassTotalRespVo {

    @Schema(name = "confirmedTotal", description = "预约总数")
    private BigDecimal confirmedTotal;

    @Schema(name = "teacherCancelledTotal", description = "老师取消总数")
    private BigDecimal teacherCancelledTotal;

    public StudentClassTotalRespVo(BigDecimal teacherCancelledTotal, BigDecimal confirmedTotal) {
        this.teacherCancelledTotal = teacherCancelledTotal;
        this.confirmedTotal = confirmedTotal;
    }
}
