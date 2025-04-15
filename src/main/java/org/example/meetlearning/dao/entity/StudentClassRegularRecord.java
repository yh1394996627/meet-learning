package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StudentClassRegularRecord {

    private Long id;

    private String creator;

    private String createName;

    private Date createTime;

    private Date courseTime;

    private String beginTime;

    private String endTime;

    private String regularId;

}