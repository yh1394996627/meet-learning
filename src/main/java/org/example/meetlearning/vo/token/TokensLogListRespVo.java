package org.example.meetlearning.vo.token;


import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class TokensLogListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "userId", description = "用户ID")
    private String userId;

    @Schema(name = "userName", description = "用户名称")
    private String userName;

    @Schema(name = "userEmail", description = "用户邮箱")
    private String userEmail;

    @Schema(name = "quantity", description = "数量")
    private BigDecimal quantity;

    @Schema(name = "remark", description = "备注")
    private String remark;

    @Schema(name = "balance", description = "余额")
    private BigDecimal balance;

    @Schema(name = "currencyCode", description = "余额")
    private BigDecimal currencyCode;

    @Schema(name = "createTime", description = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @Schema(name = "creator", description = "创建人")
    private String creator;

    @Schema(name = "createName", description = "创建人名称")
    private String createName;

}
