package org.example.meetlearning.vo.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserPayReqVo {

    @Schema(name = "userId", description = "用户ID 学生/老师/代理商")
    private String userId;

    @Schema(name = "discountRate", description = "折扣率 9折传0.9 以此类推")
    private BigDecimal discountRate;

    @Schema(name = "balanceQty", description = "可用金额")
    private BigDecimal balanceQty;

    @Schema(name = "quantity", description = "充值的课时币数量")
    private BigDecimal quantity;

    @Schema(name = "expirationTime", description = "过期时间")
    private String expirationTime;

    @Schema(name = "paymentId", description = "支付渠道ID")
    private String paymentId;

    @Schema(name = "paymentName", description = "支付渠道名称")
    private String paymentName;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "currencyName", description = "币种名称")
    private String currencyName;

    @Schema(name = "payAmount", description = "支付金额")
    private BigDecimal payAmount;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "version", description = "版本号")
    private Long version;

}
