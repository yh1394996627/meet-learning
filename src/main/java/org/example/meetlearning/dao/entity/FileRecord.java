package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class FileRecord {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String userId;

    private String fileUrl;

    private String fileName;
}