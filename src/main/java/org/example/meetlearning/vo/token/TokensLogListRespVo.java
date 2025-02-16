package org.example.meetlearning.vo.token;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TokensLogListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "quantity", description = "数量")
    private BigDecimal quantity;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

    @Schema(name = "createTime", description = "创建时间")
    private Date createTime;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

}
