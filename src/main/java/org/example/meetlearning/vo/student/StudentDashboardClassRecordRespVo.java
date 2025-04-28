package org.example.meetlearning.vo.student;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

@Data
public class StudentDashboardClassRecordRespVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date date;

    private String recordId;

    @Schema(description = "课程名称")
    private String courseName;

    @Schema(description = "课程时间")
    private String courseTime;

    @Schema(description = "课程状态")
    private String courseStatus;

}
