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

    private String studentId;

    private String studentName;

    private String studentEmail;

    private String studentCountry;

    private String teacherId;

    private String teacherName;

    private String teacherEmail;

    private String teacherCountry;

    private String courseId;

    private String courseName;

    private String courseType;

    private Date courseTime;

    private String beginTime;

    private String endTime;

    private String affiliateId;

    private String affiliateName;

    private BigDecimal studentConsumption;

    private Date efficientDate;

    private BigDecimal studentBalance;

    private Integer classStatus;

    private String cancelId;

    private Date cancelTime;

    private String meetingRecordId;

    private Boolean isComplaint;

    private Boolean isEvaluation;

    private String textbook;


}