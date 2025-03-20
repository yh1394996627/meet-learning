package org.example.meetlearning.service;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.converter.BaseConfigConverter;
import org.example.meetlearning.dao.entity.BaseConfig;
import org.example.meetlearning.enums.ConfigTypeEnum;
import org.example.meetlearning.service.impl.BaseConfigService;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.config.BaseConfigQueryVo;
import org.example.meetlearning.vo.config.BaseConfigReqVo;
import org.example.meetlearning.vo.config.BaseConfigRespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class BaseConfigPcService {

    private final BaseConfigService baseConfigService;

    public RespVo<List<BaseConfigRespVo>> findList(BaseConfigQueryVo queryVo) {
        try {
            List<BaseConfig> configs = baseConfigService.selectListByType(queryVo.getConfigType().name());
            List<BaseConfigRespVo> respVos = configs.stream().map(BaseConfigConverter.INSTANCE::toRespVo).toList();
            return new RespVo<>(respVos);
        } catch (Exception e) {
            log.error("Query failed", e);
            return new RespVo<>(null, false, "Query failed");
        }
    }

    public RespVo<String> addConfig(String userCode, BaseConfigReqVo queryVo) {
        try {
            BaseConfig codeBaseConfig = baseConfigService.selectByCode(queryVo.getCode());
            Assert.isTrue(codeBaseConfig == null, "Configuration already exists code:【" + queryVo.getCode() + "】");
            BaseConfig nameBaseConfig = baseConfigService.selectByName(queryVo.getCode());
            Assert.isTrue(nameBaseConfig == null, "Configuration already exists name:【" + queryVo.getName() + "】");
            BaseConfig symbolBaseConfig = baseConfigService.selectBySymbol(queryVo.getCode());
            Assert.isTrue(symbolBaseConfig == null, "Configuration already exists symbol:【" + queryVo.getSymbol() + "】");
            BaseConfig config = BaseConfigConverter.INSTANCE.toCreate(userCode, queryVo.getCode(), queryVo.getName(), queryVo.getSymbol(), queryVo.getConfigType().name());
            baseConfigService.insertEntity(config);
            return new RespVo<>("New configuration successfully added");
        } catch (Exception e) {
            log.error("Failed to add configuration", e);
            return new RespVo<>(null, false, "Failed to add configuration");
        }
    }


    public RespVo<String> updateConfig(String userCode, BaseConfigReqVo queryVo) {
        try {
            BaseConfig baseConfig = baseConfigService.selectByRecordId(queryVo.getRecordId());
            Assert.notNull(baseConfig, "Configuration information not obtained record:【" + queryVo.getRecordId() + "】");

            BaseConfig codeBaseConfig = baseConfigService.selectByCode(queryVo.getCode());
            if (codeBaseConfig != null) {
                Assert.isTrue(!StringUtils.equals(codeBaseConfig.getRecordId(), queryVo.getRecordId()), "Configuration already exists code:【" + queryVo.getCode() + "】");
            }
            BaseConfig nameBaseConfig = baseConfigService.selectByName(queryVo.getCode());
            if (nameBaseConfig != null) {
                Assert.isTrue(!StringUtils.equals(nameBaseConfig.getRecordId(), queryVo.getRecordId()), "Configuration already exists name:【" + queryVo.getName() + "】");
            }
            BaseConfig symbolBaseConfig = baseConfigService.selectBySymbol(queryVo.getCode());
            if (symbolBaseConfig != null) {
                Assert.isTrue(!StringUtils.equals(symbolBaseConfig.getRecordId(), queryVo.getRecordId()), "Configuration already exists symbol:【" + queryVo.getSymbol() + "】");
            }
            baseConfig.setCode(queryVo.getCode());
            baseConfig.setName(queryVo.getName());
            baseConfig.setSymbol(queryVo.getSymbol());
            baseConfig.setType(queryVo.getConfigType().name());
            baseConfigService.updateEntity(baseConfig);
            return new RespVo<>("New configuration successfully update");
        } catch (Exception e) {
            log.error("Failed to update configuration", e);
            return new RespVo<>(null, false, "Failed to update configuration");
        }
    }


    public RespVo<String> deletedConfig(RecordIdQueryVo queryVo) {
        try {
            baseConfigService.deleteByRecordId(queryVo.getRecordId());
            return new RespVo<>("Configuration deleted successfully");
        } catch (Exception e) {
            log.error("Failed to delete configuration", e);
            return new RespVo<>(null, false, "Failed to delete configurationn");
        }
    }
}
