package org.example.meetlearning.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class WxPayCreateReqVo {

    @Schema(name = "studentId", description = "学生id")
    private String studentId;

    @Schema(name = "configId", description = "支付配置id")
    private String configId;

    @Schema(name = "ipAddress", description = "支付IP地址")
    private String ipAddress;

}
