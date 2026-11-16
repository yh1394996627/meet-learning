package org.example.meetlearning.vo.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TeacherDashboardQueryVo {

    @Schema(name = "beginDate", description = "开始日期 例子:2026-11-01")
    private String beginDate;

    @Schema(name = "endDate", description = "结束日期 例子:2026-11-16")
    private String endDate;
}
