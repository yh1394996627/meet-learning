package org.example.meetlearning.dao.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.BaseConfig;

import java.util.List;

public interface BaseConfigMapper {

    int deleteByRecordId(String recordId);

    int insertEntity(BaseConfig record);

    BaseConfig selectByRecordId(String recordId);

    BaseConfig selectByCode(String code);

    BaseConfig selectByCodeAndType(@Param("code") String code, @Param("type") String type);

    BaseConfig selectByName(String name);

    BaseConfig selectByNameAndType(@Param("name") String name, @Param("type") String type);

    BaseConfig selectBySymbol(String symbol);

    BaseConfig selectBySymbolAndType(@Param("symbol") String symbol, @Param("type") String type);

    List<BaseConfig> selectByType(String type);

    int updateEntity(BaseConfig record);
}