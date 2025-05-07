package org.example.meetlearning.vo.pay;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PayRespVo {

    @Schema(name = "codeUrl", description = "二维码地址")
    private String codeUrl;

    @Schema(name = "qrCode", description = "二维码图片")
    private String qrCode;

    @Schema(name = "orderId", description = "订单ID")
    private String orderId;

}