package org.example.meetlearning.vo.finance;


import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinanceRecordRespVo {

    private String recordId;

    private String userId;

    private BigDecimal quantity;

    private BigDecimal userType;



}
