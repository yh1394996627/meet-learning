package org.example.meetlearning.service;

import cn.hutool.json.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class WechatPayService {

    @Value("${wechat.pay.appid}")
    private String appid;

    @Value("${wechat.pay.mch-id}")
    private String mchId;

    @Value("${wechat.pay.notify-url}")
    private String notifyUrl;

    @Autowired
    private CloseableHttpClient httpClient;

    // 创建Native支付订单
    public String createNativeOrder(BigDecimal courseCoins) throws Exception {

        // 构建请求体
        JSONObject requestBody = new JSONObject()
                .put("mchid", mchId)
                .put("appid", appid)
                .put("description", "课时币购买")
                .put("out_trade_no", generateOrderNo())
                .put("notify_url", notifyUrl)
                .put("amount", new JSONObject()
                        .put("total", courseCoins)
                        .put("currency", "CNY"));

        // 发送请求
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setEntity(new StringEntity(requestBody.toString()));

        try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
            String body = EntityUtils.toString(response.getEntity());
            JSONObject result = new JSONObject(body);
            return result.getStr("code_url");
        }
    }

    // 生成订单号（示例）
    private String generateOrderNo() {
        return "ORDER_" + System.currentTimeMillis() + "_" + (int)(Math.random()*1000);
    }
}