package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;


@Data
public class TeacherFeature {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String teacherId;

    private String specialists;

}