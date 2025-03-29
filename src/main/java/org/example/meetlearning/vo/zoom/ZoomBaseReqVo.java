package org.example.meetlearning.vo.zoom;


import cn.hutool.core.util.BooleanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ZoomBaseReqVo {

    @Schema(name = "recordId", description = "业务ID")
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

    @Schema(name = "type", description = "账号类型 0:免费版 1:PRO 2:商业版 3:企业版")
    private Integer type;

    @Schema(name = "userZoomId", description = "zoom 用户ID")
    private String userZoomId;

    @Schema(name = "zoomStatusMsg", description = "zoom 用户ID")
    private String zoomStatusMsg;

    @Schema(name = "isException", description = "是否异常")
    public Boolean isException;


}
