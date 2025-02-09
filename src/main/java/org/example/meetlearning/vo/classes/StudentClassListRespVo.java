package org.example.meetlearning.vo.classes;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class StudentClassListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "studentId", description = "学生ID")
    private Long studentId;

    @Schema(name = "studentName", description = "学生")
    private String studentName;

    @Schema(name = "studentLanguage", description = "学生国籍")
    private String studentLanguage;

    @Schema(name = "teacherId", description = "老师ID")
    private String teacherId;

    @Schema(name = "teacherName", description = "老师")
    private String teacherName;

    @Schema(name = "teacherLanguage", description = "老师国籍")
    private String teacherLanguage;

    @Schema(name = "courseId", description = "课程ID")
    private String courseId;

    @Schema(name = "courseName", description = "课程名称")
    private String courseName;

    @Schema(name = "courseTime", description = "上课时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date courseTime;

    @Schema(name = "courseType", description = "课型 SINGLE:单人  GROUP:团体  EXPERIENCE_TEST:团体测试")
    private String courseType;

    @Schema(name = "courseLongTime", description = "课程时常 分钟")
    private BigDecimal courseLongTime;

    @Schema(name = "teacherCourseStatus", description = "老师课程状态 ")
    private String teacherCourseStatusContent;

    @Schema(name = "studentCourseStatus", description = "学生课程状态")
    private String studentCourseStatusContent;

    @Schema(name = "courseVideoUrl", description = "课程录像")
    private String courseVideoUrl;

    @Schema(name = "isCourseVideoExpired", description = "课程录像是否失效")
    private Boolean isCourseVideoExpired;

    @Schema(name = "affiliateId", description = "代理商ID")
    private String affiliateId;

    @Schema(name = "affiliateName", description = "代理商名称")
    private String affiliateName;

    @Schema(name = "studentConsumption", description = "学生消费课时币")
    private BigDecimal studentConsumption;

    @Schema(name = "studentBalance", description = "学生课时币余额")
    private BigDecimal studentBalance;

    @Schema(name = "efficientDate", description = "产品最新有效期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date efficientDate;


}
