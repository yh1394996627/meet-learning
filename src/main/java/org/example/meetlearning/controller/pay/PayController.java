package org.example.meetlearning.controller.pay;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.service.WechatPayService;
import org.example.meetlearning.util.QrCodeGenerator;
import org.example.meetlearning.vo.pay.WxPayCreateReqVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@Slf4j
@Tag(name = "支付")
public class PayController {

    @Autowired
    private WechatPayService payService;

    // 创建支付订单
    @PostMapping(value = "/api/pay/wx/create", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> createOrder(@RequestBody WxPayCreateReqVo reqVo) {
        try {
            String codeUrl = payService.createNativeOrder(new BigDecimal(0.01));
            return ResponseEntity.ok()
                    .header("Content-Type", "application/json;charset=UTF-8")
                    .body(Map.of(
                            "code_url", codeUrl,
                            "qrcode", QrCodeGenerator.generateBase64(codeUrl, 300, 300)
                    ));
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .header("Content-Type", "text/plain;charset=UTF-8")
                    .body("支付创建失败: " + e.getMessage());
        }
    }

    // 支付结果回调（需验签）
    @PostMapping("/wx/notify")
    public String paymentNotify(@RequestBody String notifyData) {
        try {
            log.info("notifyData:{}", notifyData);
            // 1. 验证签名（SDK自动完成）
            // 2. 解析支付结果
            JSONObject result = new JSONObject(notifyData);
            String tradeNo = result.getStr("out_trade_no");
            String status = result.getStr("trade_state");

            // 3. 更新订单状态
            if ("SUCCESS".equals(status)) {
                // TODO: 处理支付成功逻辑
            }
            // 返回成功响应
            return new JSONObject().put("code", "SUCCESS").put("message", "成功").toString();
        } catch (Exception e) {
            return new JSONObject().put("code", "FAIL").put("message", "处理失败").toString();
        }
    }


}