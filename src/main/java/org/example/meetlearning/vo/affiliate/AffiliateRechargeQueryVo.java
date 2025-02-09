package org.example.meetlearning.vo.affiliate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffiliateRechargeQueryVo {

    @Schema(name = "recordId", description = "代理商recordId")
    private String recordId;

}
