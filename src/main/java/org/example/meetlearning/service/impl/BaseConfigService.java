package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.BaseConfig;
import org.example.meetlearning.dao.mapper.BaseConfigMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class BaseConfigService {

    private final BaseConfigMapper baseConfigMapper;

    public BaseConfig selectByRecordId(String recordId) {
        return baseConfigMapper.selectByRecordId(recordId);
    }

    public BaseConfig selectByCode(String code) {
        return baseConfigMapper.selectByCode(code);
    }

    public BaseConfig selectByName(String name) {
        return baseConfigMapper.selectByName(name);
    }

    public BaseConfig selectBySymbol(String symbol) {
        return baseConfigMapper.selectBySymbol(symbol);
    }

    public List<BaseConfig> selectListByType(String type) {
        return baseConfigMapper.selectByType(type);
    }

    public void deleteByRecordId(String recordId) {
        baseConfigMapper.deleteByRecordId(recordId);
    }

    public void updateEntity(BaseConfig record) {
        baseConfigMapper.updateEntity(record);
    }

    public void insertEntity(BaseConfig record) {
        baseConfigMapper.insertEntity(record);
    }

}
