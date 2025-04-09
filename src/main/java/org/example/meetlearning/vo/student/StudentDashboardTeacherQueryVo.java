package org.example.meetlearning.vo.student;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentDashboardTeacherQueryVo {

    @Schema(name = "type",description = "查询最火名师类型  1.按星级  2.按预约课的数量")
    private Integer type;


}
