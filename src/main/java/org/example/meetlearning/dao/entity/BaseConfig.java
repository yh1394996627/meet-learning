package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class BaseConfig {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String code;

    private String name;

    private String symbol;

    private String type;

}