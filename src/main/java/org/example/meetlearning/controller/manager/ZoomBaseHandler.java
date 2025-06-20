package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.service.ZoomBasePcService;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.zoom.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "ZOOM基础配置接口")
@RestController
@AllArgsConstructor
public class ZoomBaseHandler implements BaseHandler {

    private final ZoomBasePcService zoomBasePcService;

    @Operation(summary = "ZOOM列表", operationId = "zoomList")
    @PostMapping(value = "v1/zoom/base/list")
    public RespVo<List<ZoomBaseListRespVo>> zoomList(@RequestBody ZoomBaseListQueryVo queryVo) {
        return new RespVo<>(zoomBasePcService.zoomList(queryVo));
    }

    @Operation(summary = "ZOOM配置新增", operationId = "add")
    @PostMapping(value = "v1/zoom/base/add")
    public RespVo<String> zoomAdd(@RequestBody ZoomBaseReqVo reqVo) {
        zoomBasePcService.zoomAdd(getUserCode(), getUserName(), reqVo);
        return new RespVo<>("New successfully added");
    }

    @Operation(summary = "ZOOM配置更新", operationId = "zoomUpdate")
    @PostMapping(value = "v1/zoom/base/update")
    public RespVo<String> zoomUpdate(@RequestBody ZoomBaseReqVo reqVo) {
        zoomBasePcService.zoomUpdate(getUserCode(), getUserName(), reqVo);
        return new RespVo<>("Update success");
    }

    @Operation(summary = "ZOOM配置删除", operationId = "zoomDeleted")
    @PostMapping(value = "v1/zoom/base/deleted")
    public RespVo<String> zoomDeleted(@RequestBody RecordIdQueryVo reqVo) {
        zoomBasePcService.zoomDeleted(reqVo);
        return new RespVo<>("Delete successfully");
    }

    @Operation(summary = "ZOOM配置校验", operationId = "zoomVerify")
    @PostMapping(value = "v1/zoom/base/verify")
    public RespVo<ZoomBaseVerifyRespVo> zoomVerify(@RequestBody ZoomBaseStatusReqVo reqVo) {
        return new RespVo<>(zoomBasePcService.zoomVerify(reqVo));
    }

}
