package org.example.meetlearning.vo.affiliate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffiliateCommissionRecordRespVo {

    @Schema(name = "recordId", description = "记录ID")
    private String recordId;

    @Schema(name = "operaType", description = "操作类型")
    private String operaType;

    @Schema(name = "commission", description = "操作佣金")
    private BigDecimal commission;

    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

}
