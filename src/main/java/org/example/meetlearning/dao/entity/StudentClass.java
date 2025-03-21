package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;


@Data
public class StudentClass {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private Integer teacherCourseStatus;

    private Integer studentCourseStatus;

    private String courseVideoUrl;

    private String zoomId;

    private Boolean isCourseVideoExpired;

    private Long studentId;

    private String studentName;

    private String studentCountry;

    private Long teacherId;

    private String teacherName;

    private String teacherCountry;

    private String courseId;

    private String courseName;

    private String courseType;

    private BigDecimal courseLongTime;

    private Date courseTime;

    private String affiliateId;

    private String affiliateName;

    private BigDecimal studentConsumption;

    private Date efficientDate;

    private BigDecimal studentBalance;

    private Integer classStatus;

}