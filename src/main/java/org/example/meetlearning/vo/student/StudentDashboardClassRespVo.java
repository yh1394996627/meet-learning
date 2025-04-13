package org.example.meetlearning.vo.student;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class StudentDashboardClassRespVo {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    private List<StudentDashboardClassRecordRespVo> respVos;


}
