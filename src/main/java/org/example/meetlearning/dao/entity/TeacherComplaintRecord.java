package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherComplaintRecord {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String teacherId;

    private String teacherName;

    private String teacherEmail;

    private String studentId;

    private String studentName;

    private String studentEmail;

    private String studentClassId;

    private Date studentClassDate;

    private String studentClassBeginTime;

    private String studentClassEndTime;

    private BigDecimal amount;

    private BigDecimal usedAmount;

    private Boolean isCancel;

    private String remark;
}