package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import cn.hutool.core.date.DateUtil;
import org.example.meetlearning.dao.entity.PayConfig;
import org.example.meetlearning.vo.pay.PayConfigReqVo;
import org.example.meetlearning.vo.pay.PayConfigRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PayConfigConverter {

    PayConfigConverter INSTANCE = Mappers.getMapper(PayConfigConverter.class);

    default PayConfigRespVo toPayConfigRespVo(PayConfig payConfig) {
        PayConfigRespVo payConfigRespVo = new PayConfigRespVo();
        payConfigRespVo.setCreator(payConfig.getCreator());
        payConfigRespVo.setCreateTime(new Date());
        payConfigRespVo.setRecordId(payConfig.getRecordId());
        payConfigRespVo.setCurrencyId(payConfig.getCurrencyId());
        payConfigRespVo.setCurrencyCode(payConfig.getCurrencyCode());
        payConfigRespVo.setCurrencyName(payConfig.getCurrencyName());
        payConfigRespVo.setQuantity(payConfig.getQuantity());
        payConfigRespVo.setAmount(payConfig.getAmount());
        payConfigRespVo.setDays(payConfig.getDays());
        return payConfigRespVo;
    }


    default PayConfig toCreatePayConfig(String userCode, PayConfigReqVo payConfigReqVo) {
        PayConfig payConfig = new PayConfig();
        payConfig.setCreator(userCode);
        payConfig.setCreateTime(new Date());
        payConfig.setRecordId(UUID.randomUUID().toString());
        payConfig.setCurrencyCode(payConfigReqVo.getCurrencyCode());
        payConfig.setCurrencyName(payConfigReqVo.getCurrencyName());
        payConfig.setQuantity(payConfigReqVo.getQuantity());
        payConfig.setAmount(payConfigReqVo.getAmount());
        payConfig.setDays(payConfigReqVo.getExpiringDate());
        return payConfig;
    }


    default PayConfig toUpdatePayConfig(PayConfig payConfig, PayConfigReqVo payConfigReqVo) {
        payConfig.setRecordId(UUID.randomUUID().toString());
        payConfig.setCurrencyCode(payConfigReqVo.getCurrencyCode());
        payConfig.setCurrencyName(payConfigReqVo.getCurrencyName());
        payConfig.setQuantity(payConfigReqVo.getQuantity());
        payConfig.setAmount(payConfigReqVo.getAmount());
        payConfig.setDays(payConfigReqVo.getExpiringDate());
        return payConfig;
    }

}
