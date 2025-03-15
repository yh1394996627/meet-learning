package org.example.meetlearning.vo.affiliate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffiliateDashboardRespVo {

    @Schema(name = "commissionRate", description = "佣金比例")
    private BigDecimal commissionRate;

    @Schema(name = "commissionBalance", description = "余额")
    private BigDecimal commissionBalance;

    @Schema(name = "TokensBalance", description = "课时币")
    private BigDecimal TokensBalance;


}
