package org.example.meetlearning.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ManagerAmountRespVo {

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "amount", description = "金额")
    private BigDecimal amount;

    @Schema(name = "rate", description = "昨天/上月比率")
    private BigDecimal rate;

}
