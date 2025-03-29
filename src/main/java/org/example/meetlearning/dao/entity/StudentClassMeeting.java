package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class StudentClassMeeting {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String meetEmail;

    private String meetUuid;

    private String meetId;

    private String meetJoinUrl;

    private String createMeetZoomUserId;

    private String meetStatus;

    private Date meetStartTime;

    private Integer meetType;

}