package org.example.meetlearning.controller.pay;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.PayConfigPcService;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.pay.PayConfigQueryVo;
import org.example.meetlearning.vo.pay.PayConfigReqVo;
import org.example.meetlearning.vo.pay.PayConfigRespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Tag(name = "支付基础配置")
@Slf4j
@AllArgsConstructor
public class PayConfigController implements BaseController {

    private PayConfigPcService payConfigPcService;

    @Operation(summary = "支付配置列表查询", operationId = "payConfigList")
    @PostMapping(value = "v1/pay/config/list")
    public RespVo<List<PayConfigRespVo>> payConfigList(@RequestBody PayConfigQueryVo queryVo) {
        return new RespVo<>(payConfigPcService.payConfigList(queryVo));
    }

    @Operation(summary = "支付配置新增", operationId = "payConfigAdd")
    @PostMapping(value = "v1/pay/config/add")
    public RespVo<String> payConfigAdd(@RequestBody PayConfigReqVo reqVo) {
        payConfigPcService.add(getUserCode(), reqVo);
        return new RespVo<>("Operation successful");
    }


    @Operation(summary = "支付配置更新", operationId = "payConfigUpdate")
    @PostMapping(value = "v1/pay/config/update")
    public RespVo<String> payConfigUpdate(@RequestBody PayConfigReqVo reqVo) {
        payConfigPcService.update(reqVo);
        return new RespVo<>("Operation successful");
    }

    @Operation(summary = "支付配置删除", operationId = "payConfigDelete")
    @PostMapping(value = "v1/pay/config/delete")
    public RespVo<String> payConfigDelete(@RequestBody RecordIdQueryVo reqVo) {
        payConfigPcService.delete(reqVo.getRecordId());
        return new RespVo<>("Operation successful");
    }
}
