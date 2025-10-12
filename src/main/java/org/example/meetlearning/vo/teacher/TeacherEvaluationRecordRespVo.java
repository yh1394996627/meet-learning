package org.example.meetlearning.vo.teacher;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class  TeacherEvaluationRecordRespVo {

    @Schema(description = "评价记录ID")
    private String recordId;

    @Schema(description = "学生ID")
    private String studentId;

    @Schema(description = "学生名称")
    private String studentName;

    @Schema(description = "学生邮箱")
    private String studentEmail;

    @Schema(description = "评分")
    private BigDecimal rating;

    @Schema(description = "课程ID")
    private Long classId;

    @Schema(description = "备注")
    private String remark;

}
