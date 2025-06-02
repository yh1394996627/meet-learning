package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TeacherSalary {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String updator;

    private String updateName;

    private Date updateTime;

    private String recordId;

    private String teacherId;

    private String currencyRecordId;

    private String currencyCode;

    private Boolean isVerification = false;

    private BigDecimal price;

    private BigDecimal groupPrice;

    private BigDecimal confirmedQty;

    private BigDecimal oneStarQty;

    private BigDecimal absentQty;

    private BigDecimal deductionQty;

    private BigDecimal groupConfirmedQty;

    private BigDecimal groupOneStarQty;

    private BigDecimal groupAbsentQty;

    private BigDecimal groupDeductionQtyQty;

    private Date beginDate;

    private Date endDate;

    private BigDecimal confirmedAmount;

    private BigDecimal oneStarAmount;

    private BigDecimal absentAmount;

    private BigDecimal deductionAmount;

}