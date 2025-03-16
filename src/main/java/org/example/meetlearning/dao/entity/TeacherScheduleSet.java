package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TeacherScheduleSet {

    private Long id;

    private String creator;

    private Date createTime;

    private String teacherId;

    private String beginTime;

    private String endTime;

    private String weekNum;

    private String scheduleType;

    private Date offDate;
}