package org.example.meetlearning.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PayConfigQueryVo {

    @Schema(name = "currencyCode", description = "币种编码")
    private String currencyCode;

}