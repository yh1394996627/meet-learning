package org.example.meetlearning.controller.pay;

import cn.hutool.extra.qrcode.QrCodeUtil;
import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.common.QRCodeUtil;
import org.example.meetlearning.service.WechatPayService;
import org.example.meetlearning.util.QrCodeGenerator;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.pay.PayRespVo;
import org.example.meetlearning.vo.pay.WxPayCreateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@Tag(name = "支付")
public class PayController {

    @Autowired
    private WechatPayService payService;

//    @Autowired
//    private QRCodeUtil qrCodeUtil;

    // 创建支付订单
    @PostMapping(value = "/api/pay/wx/create", produces = "application/json;charset=UTF-8")
    public RespVo<PayRespVo> createOrder(@RequestBody WxPayCreateReqVo reqVo) {
        try {
            String codeUrl = payService.createOrder(reqVo);
            //String qrCode = qrCodeUtil.generateQRCode(codeUrl, 300, 300);
            return new RespVo<>(new PayRespVo(codeUrl, QrCodeGenerator.generateBase64(codeUrl, 300, 300)));
        } catch (Exception e) {
            log.error("创建支付订单失败", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }

    // 支付结果回调（需验签）
    @PostMapping("/api/pay/wx/notify")
    public String payNotify(HttpServletRequest request) throws Exception {
        return payService.paymentNotify(request);
    }
}