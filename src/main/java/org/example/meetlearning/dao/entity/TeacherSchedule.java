package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;


@Data
public class TeacherSchedule {

    private Long id;

    private String creator;

    private Date createTime;

    private String teacherId;

    private String beginTime;

    private String endTime;

    private String weekNum;

}