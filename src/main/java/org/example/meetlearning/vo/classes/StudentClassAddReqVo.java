package org.example.meetlearning.vo.classes;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentClassAddReqVo {

    @Schema(name = "studentId", description = "学生ID")
    private String studentId;

    @Schema(name = "teacherId", description = "老师ID")
    private String teacherId;

    @Schema(name = "courseDate", description = "课程日期")
    private Date courseDate;

    @Schema(name = "courseType", description = "课程类型")
    private Integer courseType;

    @Schema(name = "courseTime", description = "课程时长")
    private String courseTime;

    @Schema(name = "textbookId", description = "教材ID")
    private String textbookId;

    @Schema(name = "textbookName", description = "教材名称")
    private String textbookName;


}
