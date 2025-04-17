package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherCourseTime {

    private Long id;

    private String recordId;

    private String teacherId;

    private Date courseTime;

    private String beginTime;

    private String endTime;

    private String courseType;

}