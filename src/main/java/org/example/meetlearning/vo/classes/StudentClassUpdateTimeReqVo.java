package org.example.meetlearning.vo.classes;

import lombok.Data;

import java.util.Date;

@Data
public class StudentClassUpdateTimeReqVo {

    private String recordId;

    private Date courseDate;

    private String courseTime;

    private String beginTime;

    private String endTime;

    public String getBeginTime() {
        return courseTime.split("-")[0];
    }

    public String getEndTime() {
        return courseTime.split("-")[1];
    }

}
