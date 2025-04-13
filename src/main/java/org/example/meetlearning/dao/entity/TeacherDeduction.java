package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherDeduction {

    private Long id;

    private Boolean deleted;

    private String teacherId;

    private String classId;

    private BigDecimal deductionQty;

    private Date deductionDate;

    private String creator;

    private Date createTime;

    private String remark;

}