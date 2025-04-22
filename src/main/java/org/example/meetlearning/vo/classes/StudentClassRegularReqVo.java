package org.example.meetlearning.vo.classes;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class StudentClassRegularReqVo {

    private String teacherId;

    private String studentId;

    @Schema(name = "courseId", description = "课程ID")
    private String courseId;

    @Schema(name = "courseName", description = "课程名称")
    private String courseName;

    @Schema(name = "courseType", description = "课程类型")
    private String courseType;

    @Schema(name = "courseTimes", description = "课程时间集合 起始-结束")
    private List<String> courseTimes;

    @Schema(name = "courseDates", description = "课程日期 yyy-MM-dd")
    private List<String> courseDates;

}
