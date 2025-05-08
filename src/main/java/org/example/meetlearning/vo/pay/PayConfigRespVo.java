package org.example.meetlearning.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayConfigRespVo {

    private Long id;

    private String creator;

    private Date createTime;

    private String recordId;

    private String currencyId;

    @Schema(name = "currencyCode", description = "币种编码")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "quantity", description = "课时币数量")
    private BigDecimal quantity;

    @Schema(name = "amount", description = "充值金额")
    private BigDecimal amount;

    @Schema(name = "days", description = "生效天数")
    private Integer days;

}