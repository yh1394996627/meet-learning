package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.BaseConfig;

import java.util.List;

public interface BaseConfigMapper {

    int deleteByRecordId(String recordId);

    int insertEntity(BaseConfig record);

    BaseConfig selectByRecordId(String recordId);

    BaseConfig selectByCode(String code);

    BaseConfig selectByName(String name);

    BaseConfig selectBySymbol(String symbol);

    List<BaseConfig> selectByType(String type);

    int updateEntity(BaseConfig record);
}