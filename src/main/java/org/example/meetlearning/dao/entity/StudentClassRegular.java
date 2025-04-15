package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentClassRegular {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String recordId;

    private String studentId;

    private String studentName;

    private String studentEmail;

    private String teacherId;

    private String teacherName;

    private String courseId;

    private String courseName;

    private String courseType;

    private Date courseTime;

    private String beginTime;

    private String endTime;

    private Integer auditStatus;

    private BigDecimal price;

    private BigDecimal creditsPrice;

}