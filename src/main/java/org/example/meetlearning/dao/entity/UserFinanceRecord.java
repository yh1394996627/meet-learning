package org.example.meetlearning.dao.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UserFinanceRecord {

    private Long id;

    private Boolean deleted;

    private String creator;

    private String createName;

    private Date createTime;

    private String recordId;

    private String userId;

    private BigDecimal quantity;

    private BigDecimal usedQty;

    private BigDecimal canQty;

    private String userType;

    private Date expirationTime;

    private String currencyCode;

    private String currencyName;

    private BigDecimal payAmount;

    private String paymentId;

    private String paymentName;

    private String remark;

    private BigDecimal discountRate;

}