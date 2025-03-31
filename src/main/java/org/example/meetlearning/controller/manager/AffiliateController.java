package org.example.meetlearning.controller.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.WriterException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.AffiliatePcService;
import org.example.meetlearning.service.impl.AffiliateService;
import org.example.meetlearning.vo.affiliate.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;


@Tag(name = "代理商接口")
@RestController
@Slf4j
@AllArgsConstructor
public class AffiliateController implements BaseController {

    private final AffiliatePcService affiliatePcService;

    @Operation(summary = "代理商列表", operationId = "affiliatePage")
    @PostMapping(value = "v1/affiliate/page")
    public RespVo<PageVo<AffiliateListPageRespVo>> affiliatePage(@RequestBody AffiliateQueryVo queryVo) {
        return affiliatePcService.affiliatePage(queryVo);
    }

    @Operation(summary = "新增代理商", operationId = "affiliateAdd")
    @PostMapping(value = "v1/affiliate/add")
    public RespVo<String> affiliateAdd(@RequestBody AffiliateAddReqVo reqVo) {
        return affiliatePcService.affiliateAdd(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "修改代理商", operationId = "affiliateUpdate")
    @PostMapping(value = "v1/affiliate/update")
    public RespVo<String> affiliateUpdate(@RequestBody AffiliateUpdateReqVo reqVo) {
        return affiliatePcService.affiliateUpdate(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "修改代理商备注", operationId = "affiliateUpdateRemark")
    @PostMapping(value = "v1/affiliate/update/remark")
    public RespVo<String> affiliateUpdateRemark(@RequestBody AffiliateRemarkUpdateReqVo reqVo) {
        return affiliatePcService.affiliateUpdateRemark(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "代理商(生效的)选择查询 key,value", operationId = "affiliateSelect")
    @PostMapping(value = "v1/affiliate/select")
    public RespVo<List<SelectValueVo>> affiliateSelect(@RequestBody RecordIdQueryVo reqVo) {
        return affiliatePcService.affiliateSelect(reqVo);
    }


    @Operation(summary = "代理商仪表盘", operationId = "dashboard")
    @PostMapping(value = "v1/affiliate/dashboard")
    public RespVo<AffiliateDashboardRespVo> dashboard() {
        return affiliatePcService.dashboard(getUserCode());
    }


    @Operation(summary = "代理商佣金记录", operationId = "commission")
    @PostMapping(value = "v1/affiliate/commission/record")
    public RespVo<PageVo<AffiliateCommissionRecordRespVo>> commissionRecord() {
        AffiliateCommissionRecordRespVo respVo = new AffiliateCommissionRecordRespVo();
        respVo.setRecordId("12312312321");
        respVo.setOperaType("提现");
        respVo.setCommission(new BigDecimal("20"));
        respVo.setBalance(new BigDecimal("80"));
        Page<AffiliateCommissionRecordRespVo> page = new Page<>(0, 20, 1);
        page.setRecords(List.of(respVo));
        PageVo<AffiliateCommissionRecordRespVo> pageVo = PageVo.map(page, list -> list);
        return new RespVo<>(pageVo);
    }

    @Operation(summary = "代理商二維碼返回", operationId = "affiliateQrcode")
    @PostMapping(value = "v1/affiliate/qrcode")
    public RespVo<String> affiliateQrcode(@RequestBody RecordIdQueryVo reqVo) throws IOException, WriterException {
        return new RespVo<>(affiliatePcService.affiliateQrcode(reqVo.getRecordId()));
    }


}