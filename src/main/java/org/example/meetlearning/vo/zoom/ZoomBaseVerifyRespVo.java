package org.example.meetlearning.vo.zoom;


import cn.hutool.core.util.BooleanUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ZoomBaseVerifyRespVo {

    @Schema(name = "status", description = "状态 true校验通过")
    private Boolean status;

    @Schema(name = "code", description = "状态码")
    private Integer code;

    @Schema(name = "message", description = "状态信息")
    private String message;

    @Schema(name = "userZoomId", description = "zoom 用户ID")
    private String userZoomId;


    public String getZoomStatusMsg() {
        return message;
    }

    public Boolean getIsException() {
        return BooleanUtil.isFalse(status);
    }


}
