package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;


@Data
public class ZoomCallbackMsg {

    private Long id;

    private Boolean deleted;

    private Date createTime;

    private String recordId;

    private String name;

    private String token;

    private String payload;

}