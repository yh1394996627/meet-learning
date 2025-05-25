package org.example.meetlearning.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayConfigReqVo {

    @Schema(name = "recordId", description = "业务ID更新用")
    private String recordId;

    @Schema(name = "currencyCode", description = "币种编码")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "quantity", description = "课时币数量")
    private BigDecimal quantity;

    @Schema(name = "amount", description = "充值金额")
    private BigDecimal amount;

    @Schema(name = "expiringDate", description = "生效天数")
    private Integer expiringDate;

}