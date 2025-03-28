package org.example.meetlearning.vo.classes;

import lombok.Data;

import java.util.Date;

@Data
public class StudentClassUpdateTimeReqVo {

    private String recordId;

    private Date courseDate;

    private String beginTime;

    private String endTime;
}
