package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class MeetingLog {

    private Long id;

    private String creator;

    private String createName;

    private Date createTime;

    private String meetingId;

    private String remark;

}