package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherClassRemark {

    private Long id;

    private String creator;

    private String createName;

    private Date createTime;

    private String recordId;

    private String classId;

    private String teacherId;

    private String studentId;

    private BigDecimal filePage;

    private String classRemark;

    private String remark;

    private String teacherName;

    private Date classTime;

    private String textbook;
}