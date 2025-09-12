package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class GroupClassStudentRec {

    private Long id;

    private Boolean deleted;

    private String creator;

    private Date createTime;

    private String updator;

    private Date updateTime;

    private String classId;

    private String studentId;

    private String studentName;

    private Integer studentCourseStatus;
}