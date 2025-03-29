package org.example.meetlearning.dao.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class ZoomAccountSet {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String zoomEmail;

    private String zoomAccountId;

    private String zoomClientId;

    private String zoomClientSecret;

    private String zoomUserId;

    private String cbToken;

    private Boolean isException;

    private String zoomStatusMsg;

    private Integer totalUsedQty;

    private Integer usedQty;

    private Integer zoomType;

}