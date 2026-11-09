package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FinanceRecordDeductVo {

    @Schema(name = "id", description = "user_finance_record 自增id")
    private Long id;

    @Schema(name = "qty", description = "本条record扣减数量(正数)")
    private BigDecimal qty;

}
