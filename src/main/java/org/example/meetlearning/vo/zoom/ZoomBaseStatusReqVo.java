package org.example.meetlearning.vo.zoom;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ZoomBaseStatusReqVo {

    @Schema(name = "recordId", description = "业务ID  列表校验")
    private String recordId;

    @Schema(name = "email", description = "邮箱")
    private String email;

    @Schema(name = "accountId", description = "accountId")
    private String accountId;

    @Schema(name = "clientId", description = "clientId")
    private String clientId;

    @Schema(name = "clientSecret", description = "clientSecret")
    private String clientSecret;


}
