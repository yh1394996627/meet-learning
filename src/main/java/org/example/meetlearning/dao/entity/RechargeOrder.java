package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class RechargeOrder {

    private Long id;

    private String orderId;

    private Date createTime;

    private Date payTime;

    private String studentId;

    private String managerId;

    private BigDecimal amount;

    private BigDecimal quantity;

    private String ipAddress;

    private String wxTransactionId;

    private String payConfigId;

    private Integer status;

}