package org.example.meetlearning.vo.affiliate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffiliateRechargeRecordRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "amount", description = "课时币")
    private BigDecimal amount;

    @Schema(name = "balanceAfter", description = "充值余额")
    private BigDecimal balanceAfter;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

}
