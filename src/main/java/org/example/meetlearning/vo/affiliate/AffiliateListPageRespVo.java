package org.example.meetlearning.vo.affiliate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class AffiliateListPageRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "name", description = "姓名")
    private String name;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "balance", description = "课时币余额")
    private BigDecimal balance;

    @Schema(name = "commissionRate", description = "佣金率")
    private BigDecimal commissionRate;

    @Schema(name = "rate", description = "比率")
    private BigDecimal rate;

    @Schema(name = "studentTotal", description = "学生总数")
    private BigDecimal studentTotal;

    @Schema(name = "courseTotal", description = "课程总数")
    private BigDecimal courseTotal;

    @Schema(name = "remark", description = "备注")
    private String remark;



}
