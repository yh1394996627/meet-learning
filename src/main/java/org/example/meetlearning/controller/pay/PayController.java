package org.example.meetlearning.controller.pay;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.WechatPayService;
import org.example.meetlearning.util.QrCodeGenerator;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.pay.PayRespVo;
import org.example.meetlearning.vo.pay.WxPayCreateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
@Tag(name = "支付")
public class PayController {

    @Autowired
    private WechatPayService payService;

    // 创建支付订单
    @PostMapping(value = "/api/pay/wx/create", produces = "application/json;charset=UTF-8")
    public RespVo<PayRespVo> createOrder(@RequestBody WxPayCreateReqVo reqVo) {
        try {
            Map<String, String> resultMap = payService.createOrder(reqVo);
            if (resultMap == null) {
                return new RespVo<>(new PayRespVo(null, null, null));
            }
            String codeUrl = resultMap.get("codeUrl");
            String orderId = resultMap.get("orderId");
            return new RespVo<>(new PayRespVo(codeUrl, QrCodeGenerator.generateBase64(codeUrl, 300, 300), orderId));
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


    @PostMapping("/api/pay/wx/status")
    public RespVo<Boolean> patStatus(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(payService.patStatus(queryVo));
    }
}