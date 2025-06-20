package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.service.BaseConfigPcService;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.config.BaseConfigQueryVo;
import org.example.meetlearning.vo.config.BaseConfigReqVo;
import org.example.meetlearning.vo.config.BaseConfigRespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "基础配置")
@RestController
@Slf4j
@AllArgsConstructor
public class BaseConfigHandler implements BaseHandler {

    private final BaseConfigPcService baseConfigPcService;


    @Operation(summary = "基础配置查询", operationId = "sharedTeacherList")
    @PostMapping(value = "v1/config/list")
    public RespVo<List<BaseConfigRespVo>> findList(@RequestBody BaseConfigQueryVo queryVo) {
        return baseConfigPcService.findList(queryVo);
    }

    @Operation(summary = "基础配置新增", operationId = "addConfig")
    @PostMapping(value = "v1/config/add")
    public RespVo<String> addConfig(@RequestBody BaseConfigReqVo reqVo) {
        return baseConfigPcService.addConfig(getUserCode(), reqVo);
    }

    @Operation(summary = "基础配置更新", operationId = "updateConfig")
    @PostMapping(value = "v1/config/update")
    public RespVo<String> updateConfig(@RequestBody BaseConfigReqVo reqVo) {
        return baseConfigPcService.updateConfig(getUserCode(), reqVo);
    }

    @Operation(summary = "基础配置删除", operationId = "deletedConfig")
    @PostMapping(value = "v1/config/deleted")
    public RespVo<String> deletedConfig(@RequestBody RecordIdQueryVo reqVo) {
        return baseConfigPcService.deletedConfig(reqVo);
    }

    @Operation(summary = "基础配置查询 key value", operationId = "selectConfig")
    @PostMapping(value = "v1/config/select")
    public RespVo<List<SelectValueVo>> selectConfig(@RequestBody BaseConfigQueryVo queryVo) {
        return baseConfigPcService.selectConfig(queryVo);
    }

}
