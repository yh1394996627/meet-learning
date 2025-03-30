package org.example.meetlearning.vo.classes;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class StudentClassRegularRespVo {

    private String recordId;

    private String studentId;

    private String studentName;

    private String studentEmail;

    @Schema(name = "courseType", description = "课程类型")
    private String courseType;

    @Schema(name = "courseTime", description = "课程时间 起始-结束")
    private String courseTime;

    @Schema(name = "courseDateList", description = "课程日期集合 ；分割")
    private String courseDateList;

}
