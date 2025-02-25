package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;


@Data
public class User {

    private Long id;

    private Boolean deleted;

    private Boolean isManager;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String accountCode;

    private String password;

    private String type;

    private String name;

    private String enName;

    private String email;

    private Boolean enabled;

    private String remark;

}