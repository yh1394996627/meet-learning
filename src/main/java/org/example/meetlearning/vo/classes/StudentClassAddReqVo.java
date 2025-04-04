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

    @Schema(name = "courseId", description = "课程ID")
    private String courseId;

    @Schema(name = "courseName", description = "课程名称")
    private String courseName;

    @Schema(name = "courseTime", description = "课程时长")
    private String courseTime;

    @Schema(name = "quantity", description = "消费代币数量")
    private BigDecimal quantity;


}
