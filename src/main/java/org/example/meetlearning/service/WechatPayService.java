package org.example.meetlearning.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.BooleanUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.common.WechatPayConfig;
import org.example.meetlearning.converter.RechargeOrderConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.PayStatusEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.enums.TokenContentEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.pay.WxPayCreateReqVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.example.meetlearning.util.SecureXmlUtils.mapToXml;
import static org.example.meetlearning.util.SecureXmlUtils.xmlToMap;


@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class WechatPayService extends BasePcService {

    private final WechatPayConfig config;

    private final PayConfigService payConfigService;

    private final StudentService studentService;

    private final RechargeOrderService rechargeOrderService;

    private final UserService userService;

    private final UserFinanceService userFinanceService;

    private final AffiliateService affiliateService;

    // 统一下单接口URL
    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";


    public Map<String, String> createOrder(WxPayCreateReqVo reqVo) throws Exception {
        Map<String, String> resultMap = new HashMap<>();
        //查询基础配置
        PayConfig payConfig = payConfigService.getPayConfigByRecordId(reqVo.getConfigId());
        if (payConfig == null) {
            return null;
        }
        Student student = studentService.findByRecordId(reqVo.getStudentId());
        Assert.notNull(student, "Student information not obtained");
        //生成支付订单
        RechargeOrder rechargeOrder = RechargeOrderConverter.INSTANCE.toCreate(student, payConfig, reqVo.getIpAddress());

        Map<String, String> params = new HashMap<>();
        params.put("appid", config.getAppId());
        params.put("mch_id", config.getMchId());
        params.put("nonce_str", generateNonceStr());
        params.put("body", "coin recharge");
        params.put("out_trade_no", rechargeOrder.getOrderId());
        params.put("total_fee", String.valueOf(rechargeOrder.getAmount().multiply(new BigDecimal(100)).intValue()));
        params.put("spbill_create_ip", rechargeOrder.getIpAddress());
        params.put("notify_url", config.getNotifyUrl());
        params.put("trade_type", config.getTradeType());
        // 生成签名
        String sign = generateSignature(params, config.getApiKey());
        params.put("sign", sign);
        // 转换为XML
        String xml = mapToXml(params);
        // 发送请求
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(UNIFIED_ORDER_URL);
        httpPost.setEntity(new StringEntity(xml, "UTF-8"));
        HttpResponse response = client.execute(httpPost);
        // 解析响应
        String result = EntityUtils.toString(response.getEntity(), "UTF-8");
        Map<String, String> respMap = xmlToMap(result);
        resultMap.put("orderId", rechargeOrder.getOrderId());
        if ("SUCCESS".equals(respMap.get("return_code"))) {
            String codeUrl = respMap.get("code_url");
            //添加支付订单
            rechargeOrderService.insert(rechargeOrder);
            resultMap.put("codeUrl", codeUrl);
            return resultMap;
        } else {
            throw new RuntimeException("微信下单失败: " + respMap.get("return_msg"));
        }
    }

    // 生成随机字符串
    private String generateNonceStr() {
        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
    }

    // 生成签名（MD5）
    private String generateSignature(Map<String, String> params, String apiKey) {
        // 按参数名ASCII字典序排序
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (String key : keys) {
            if (!key.equals("sign") && params.get(key) != null && !params.get(key).isEmpty()) {
                sb.append(key).append("=").append(params.get(key)).append("&");
            }
        }
        sb.append("key=").append(apiKey);
        return DigestUtils.md5Hex(sb.toString()).toUpperCase();
    }

    // XML与Map互转（需实现xmlToMap和mapToXml方法）
    public boolean handlePaymentNotify(String orderId, String transactionId) {
        RechargeOrder order = rechargeOrderService.selectByRecordId(orderId);
        // 检查订单状态
        if (!Objects.equals(order.getStatus(), PayStatusEnum.CREATED.getStatus())) {
            return false;
        }
        // 更新订单状态
        RechargeOrder rechargeOrder = new RechargeOrder();
        rechargeOrder.setId(order.getId());
        rechargeOrder.setPayTime(new Date());
        rechargeOrder.setWxTransactionId(transactionId);
        rechargeOrder.setStatus(PayStatusEnum.SUCCESS.getStatus());
        rechargeOrderService.update(rechargeOrder);
        //学生添加课时币，如果有代理商添加佣金
        User user = userService.selectByRecordId(order.getStudentId());
        PayConfig payConfig = payConfigService.getPayConfigByRecordId(order.getPayConfigId());
        Date expiringDate = DateUtil.offsetDay(new Date(), payConfig.getDays());
        operaTokenLogs(user.getRecordId(), user.getName(), user.getRecordId(), order.getQuantity(), TokenContentEnum.WECHAT_RECHARGE.getEnContent(), payConfig, rechargeOrder, expiringDate, null);
        if (StringUtils.isNotEmpty(user.getManagerId())) {
            User manager = userService.selectByRecordId(user.getManagerId());
            if (manager != null && StringUtils.equals(manager.getType(), RoleEnum.AFFILIATE.name())) {
                Affiliate affiliate = affiliateService.findByRecordId(manager.getRecordId());
                UserFinance userFinance = userFinanceService.selectByUserId(manager.getRecordId());
                userFinance.setAmount(BigDecimalUtil.nullOrZero(userFinance.getAmount()).add(BigDecimalUtil.nullOrZero(affiliate.getCommissionRate()).multiply(order.getAmount())));
                userFinanceService.updateByEntity(userFinance);
            }
        }
        return true;
    }


    public String paymentNotify(HttpServletRequest request) throws Exception {
        // 读取XML数据
        String xml = IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
        Map<String, String> params = xmlToMap(xml);
        log.info("params->:{}", params);
        // 验证签名
        String sign = params.get("sign");
        params.remove("sign");
        String calculatedSign = generateSignature(params, config.getApiKey());
        if (!sign.equals(calculatedSign)) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
        // 处理业务逻辑
        if ("SUCCESS".equals(params.get("result_code"))) {
            String orderId = params.get("out_trade_no");
            String transactionId = params.get("transaction_id");
            boolean success = handlePaymentNotify(orderId, transactionId);
            return success ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>"
                    : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
        return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
    }


    public String paymentNotify111(Map<String, String> params) throws Exception {
        // 读取XML数据
        log.info("params->:{}", params);
        // 验证签名
        String sign = params.get("sign");
        params.remove("sign");
        String calculatedSign = generateSignature(params, config.getApiKey());
        if (!sign.equals(calculatedSign)) {
            return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
        // 处理业务逻辑
        if ("SUCCESS".equals(params.get("result_code"))) {
            String orderId = params.get("out_trade_no");
            String transactionId = params.get("transaction_id");
            boolean success = handlePaymentNotify(orderId, transactionId);
            return success ? "<xml><return_code><![CDATA[SUCCESS]]></return_code></xml>"
                    : "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
        }
        return "<xml><return_code><![CDATA[FAIL]]></return_code></xml>";
    }


    public Boolean patStatus(RecordIdQueryVo queryVo) {
        RechargeOrder rechargeOrder = rechargeOrderService.selectByRecordId(queryVo.getRecordId());
        return BooleanUtil.isTrue(Objects.equals(rechargeOrder.getStatus(), PayStatusEnum.SUCCESS.getStatus()));
    }

}