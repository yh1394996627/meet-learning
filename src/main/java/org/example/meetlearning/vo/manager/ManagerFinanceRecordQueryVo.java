package org.example.meetlearning.vo.manager;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.vo.common.PageRequestQuery;

import java.util.HashMap;
import java.util.Map;

@Data
public class ManagerFinanceRecordQueryVo extends PageRequestQuery<UserFinanceRecord> {


    @Schema(name = "income", description = "类型  1:天  2:月")
    private Integer income;

    @Schema(name = "day", description = "按天查询需要传 例子:2025-01-01")
    private String day;

    @Schema(name = "yearMonth", description = "按月查询需要传 例子:2025-01")
    private String yearMonth;

    @Schema(name = "currencyCode", description = "币种")
    private String currencyCode;

    @Schema(name = "paymentId", description = "支付渠道")
    private String paymentId;

    @Schema(name = "affiliateId", description = "代理商")
    private String affiliateId;

    public Map<String, Object> getParams(String userCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("creator", userCode);
        params.put("userType", RoleEnum.STUDENT.name());
        if (income != null) {
            params.put("income", income);
        }
        if (StringUtils.isNotEmpty(day)) {
            params.put("incomeDate", day);
        }
        if (StringUtils.isNotEmpty(yearMonth)) {
            params.put("incomeDate", yearMonth);
        }
        if (StringUtils.isNotEmpty(currencyCode)) {
            params.put("currencyCode", currencyCode);
        }
        if (StringUtils.isNotEmpty(paymentId)) {
            params.put("paymentId", paymentId);
        }
        if (StringUtils.isNotEmpty(affiliateId)) {
            params.put("affiliateId", affiliateId);
        }
        return params;
    }
}
