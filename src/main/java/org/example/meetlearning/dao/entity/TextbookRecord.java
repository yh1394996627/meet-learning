package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.util.Date;

@Data
public class TextbookRecord {

    private Long id;

    private String creator;

    private String createName;

    private Date createTime;

    private String textbookId;

    private String textbookName;

    private String name;

    private String catalog;

}