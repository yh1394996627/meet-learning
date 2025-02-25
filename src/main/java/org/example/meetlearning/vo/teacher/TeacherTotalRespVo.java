package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TeacherTotalRespVo {

    @Schema(name = "totalSalary", description = "工资统计")
    private BigDecimal totalSalary;

    @Schema(name = "teacherTotalSalary", description = "老师工资统计")
    private BigDecimal teacherTotalSalary;

    @Schema(name = "managerTotalSalary", description = "管理者工资统计")
    private BigDecimal managerTotalSalary;




}
