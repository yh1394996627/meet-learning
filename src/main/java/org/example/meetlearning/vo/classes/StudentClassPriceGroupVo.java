package org.example.meetlearning.vo.classes;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class StudentClassPriceGroupVo {

    private String recordId;

    private String teacherId;

    private BigDecimal price;

    private BigDecimal groupPrice;

    private Integer classStatus;

    private String courseType;

    private BigDecimal totalQty;

}
