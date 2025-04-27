//package org.example.meetlearning.controller.pay;
//
//
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.math.BigDecimal;
//import java.util.HashMap;
//import java.util.Map;
//
//@Tag(name = "微信支付接口")
//@RestController
//@Slf4j
//@AllArgsConstructor
//public class WeiXinPayController {
//
//    @PostMapping("/generateQR")
//    public ResponseEntity<String> generateQRCode(@RequestParam BigDecimal amount,
//                                                 @RequestParam Long agentId) {
//        // 1. 生成唯一订单号
//        String orderId = UUID.randomUUID().toString().replace("-", "");
//
//        // 2. 调用微信统一下单API（需指定子商户）
//        Map<String, String> params = new HashMap<>();
//        params.put("appid", WX_APPID);
//        params.put("mch_id", WX_MCH_ID);
//        params.put("sub_mch_id", getSubMchIdByAgent(agentId)); // 根据代理获取子商户号
//        params.put("total_fee", amount.multiply(100).intValue() + "");
//        params.put("out_trade_no", orderId);
//        params.put("trade_type", "NATIVE");
//
//        // 3. 签名并发送请求
//        String response = WxPayApi.unifiedOrder(params);
//        String qrCodeUrl = parseXmlResponse(response, "code_url");
//
//        // 4. 保存订单到数据库
//        paymentOrderService.createOrder(orderId, currentUser.getId(), agentId, amount);
//
//        return ResponseEntity.ok(qrCodeUrl);
//    }
//
//
//
//
//}
