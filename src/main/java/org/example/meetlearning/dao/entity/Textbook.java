package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class Textbook {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String name;

    private Integer levelBegin;

    private Integer levelEnd;

    private String type;

}