package org.example.meetlearning.vo.teacher;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherEvaluationRecordRespVo {

    private String recordId;

    private String studentId;

    private String studentName;

    private String studentEmail;

    private BigDecimal rating;

    private Long classId;

    private String remark;

}
