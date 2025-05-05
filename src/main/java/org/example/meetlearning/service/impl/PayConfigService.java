package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.PayConfig;
import org.example.meetlearning.dao.mapper.PayConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PayConfigService {

    private final PayConfigMapper payConfigMapper;


    /**
     * 根据币种获取支付配置
     */
    public List<PayConfig> getPayConfigByCurrency(String currencyCode) {
        return payConfigMapper.selectByCurrency(currencyCode);
    }

    /**
     * 根据记录id获取支付配置
     */
    public PayConfig getPayConfigByRecordId(String recordId) {
        return payConfigMapper.selectByRecordId(recordId);
    }

    /**
     * 更新支付配置
     */
    public int updatePayConfig(PayConfig payConfig) {
        return payConfigMapper.updateEntity(payConfig);
    }

    /**
     * 删除支付配置
     */
    public int deletePayConfig(String recordId) {
        return payConfigMapper.deleteByRecordId(recordId);
    }

    /**
     * 新增支付配置
     */
    public int insertPayConfig(PayConfig payConfig) {
        return payConfigMapper.insert(payConfig);
    }


}
