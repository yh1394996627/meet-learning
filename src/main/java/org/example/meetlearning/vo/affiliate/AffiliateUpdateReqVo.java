package org.example.meetlearning.vo.affiliate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffiliateUpdateReqVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "commissionRate", description = "佣金率")
    private BigDecimal commissionRate;

    @Schema(name = "recommenderId", description = "推荐人ID")
    private String recommenderId;

    @Schema(name = "recommender", description = "推荐人")
    private String recommender;
}
