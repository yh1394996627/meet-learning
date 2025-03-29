package org.example.meetlearning.vo.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class ManagerFinanceRecordQueryVo extends PageRequestQuery<UserFinanceRecord> {

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
        if (!StringUtils.isEmpty(currencyCode)) {
            params.put("currencyCode", currencyCode);
        }
        if (!StringUtils.isEmpty(paymentId)) {
            params.put("paymentId", paymentId);
        }
        if (!StringUtils.isEmpty(affiliateId)) {
            params.put("affiliateId", affiliateId);
        }
        return params;
    }
}
