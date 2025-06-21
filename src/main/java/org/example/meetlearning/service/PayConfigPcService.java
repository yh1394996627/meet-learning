package org.example.meetlearning.service;

import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.PayConfigConverter;
import org.example.meetlearning.dao.entity.PayConfig;
import org.example.meetlearning.enums.LanguageContextEnum;
import org.example.meetlearning.service.impl.PayConfigService;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.pay.PayConfigQueryVo;
import org.example.meetlearning.vo.pay.PayConfigReqVo;
import org.example.meetlearning.vo.pay.PayConfigRespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
public class PayConfigPcService extends BasePcService{

    private final PayConfigService payConfigService;


    public List<PayConfigRespVo> payConfigList(PayConfigQueryVo queryVo) {
        List<PayConfig> payConfigs = payConfigService.getPayConfigByCurrency(queryVo.getCurrencyCode());
        return payConfigs.stream().map(PayConfigConverter.INSTANCE::toPayConfigRespVo).toList();
    }


    public void add(String userCode, PayConfigReqVo reqVo) {
        PayConfig payConfig = PayConfigConverter.INSTANCE.toCreatePayConfig(userCode, reqVo);
        payConfigService.insertPayConfig(payConfig);
    }

    public void update(PayConfigReqVo reqVo) {
        PayConfig payConfig = payConfigService.getPayConfigByRecordId(reqVo.getRecordId());
        Assert.notNull(payConfig, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        payConfig = PayConfigConverter.INSTANCE.toUpdatePayConfig(payConfig, reqVo);
        payConfigService.updatePayConfig(payConfig);
    }

    public void delete(String recordId) {
        payConfigService.deletePayConfig(recordId);
    }

}
