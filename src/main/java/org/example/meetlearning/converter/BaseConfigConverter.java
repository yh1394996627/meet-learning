package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.BaseConfig;
import org.example.meetlearning.vo.config.BaseConfigRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface BaseConfigConverter {

    BaseConfigConverter INSTANCE = Mappers.getMapper(BaseConfigConverter.class);

    default BaseConfigRespVo toRespVo(BaseConfig record) {
        BaseConfigRespVo respVo = new BaseConfigRespVo();
        respVo.setRecordId(record.getRecordId());
        respVo.setCode(record.getCode());
        respVo.setName(record.getName());
        respVo.setType(record.getType());
        respVo.setRate(record.getRate());
        return respVo;
    }

    default BaseConfig toCreate(String userCode, String code, String name, String symbol, String type, BigDecimal rate) {
        BaseConfig record = new BaseConfig();
        record.setCreator(userCode);
        record.setCreateTime(new Date());
        record.setRecordId(UUID.randomUUID().toString());
        record.setCode(code);
        record.setName(name);
        record.setSymbol(symbol);
        record.setType(type);
        record.setRate(rate);
        return record;
    }

}
