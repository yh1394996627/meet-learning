package org.example.meetlearning.vo.manager;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ManagerIncomeStatisticsRespVo {

    @Schema(name = "transactions", description = "交易数")
    private Integer transactions;

    @Schema(name = "amountList", description = "总交易金额 按币种分组")
    private List<ManagerAmountRespVo> amountList;

    @Schema(name = "amountList", description = "学生交易金额 按币种分组")
    private List<ManagerAmountRespVo> studentAmountList;


    @Schema(name = "balanceQty", description = "余额")
    private BigDecimal balanceQty;

}
