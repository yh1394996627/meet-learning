package org.example.meetlearning.vo.token;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TokensLogAddReqVo {

    @Schema(name = "userId", description = "业务ID")
    private String userId;

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "quantity", description = "数量")
    private BigDecimal quantity;

    @Schema(name = "paymentCode", description = "支付方式")
    private String paymentCode;

    @Schema(name = "currencyCode", description = "货币Code")
    private String currencyCode;

    @Schema(name = "currencyName", description = "货币名称")
    private String currencyName;

    @Schema(name = "amount", description = "支付金额")
    private BigDecimal amount;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "isDeletedCallBack", description = "是否是删除回退")
    private Boolean isDeletedCallBack;


}
