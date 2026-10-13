package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserFinanceRecordRespVo {

    @Schema(name = "id", description = "record自增id")
    private Long id;

    @Schema(name = "userId", description = "用户ID")
    private String userId;

    @Schema(name = "quantity", description = "总数量")
    private BigDecimal quantity;

    @Schema(name = "canQty", description = "可用数量")
    private BigDecimal canQty;

    @Schema(name = "usedQty", description = "已使用数量")
    private BigDecimal usedQty;

}
