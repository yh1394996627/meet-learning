package org.example.meetlearning.vo.remark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherClassRemarkPageRespVo {

    @Schema(name = "recordId", description = "记录id")
    private String recordId;

    @Schema(name = "teacherId", description = "教师id")
    private String teacherId;

    @Schema(name = "classTime", description = "上课时间")
    private Date classTime;

    @Schema(name = "textbook", description = "教材")
    private String textbook;

    @Schema(name = "teacherName", description = "教师姓名")
    private String teacherName;

    @Schema(name = "filePage", description = "文件页数")
    private BigDecimal filePage;

    @Schema(name = "classRemark", description = "上课备注")
    private String classRemark;

    @Schema(name = "remark", description = "备注")
    private String remark;

}
