package org.example.meetlearning.vo.zoom;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ZoomBaseListRespVo {

    @Schema(name = "recordId", description = "业务Id")
    private String recordId;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "accountId", description = "accountId")
    private String accountId;

    @Schema(name = "clientId", description = "clientId")
    private String clientId;

    @Schema(name = "clientSecret", description = "clientSecret")
    private String clientSecret;

    @Schema(name = "token", description = "token")
    private String token;

    @Schema(name = "type", description = "类型  0:免费版 1:PRO 2:商业版 3:企业版")
    private Integer type;

    @Schema(name = "usedQty", description = "今天使用数量")
    private Integer usedQty;

    @Schema(name = "totalUsedQty", description = "总使用数量")
    private Integer totalUsedQty;

    @Schema(name = "isException", description = "是否是异常")
    private Boolean isException;

    @Schema(name = "statusMsg", description = "状态信息")
    private String statusMsg;

    @Schema(name = "secretToken", description = "回调secretToken")
    private String secretToken;

    @Schema(name = "verificationToken", description = "回调verificationToken")
    private String verificationToken;

}
