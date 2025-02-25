package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.AffiliatePcService;
import org.example.meetlearning.service.impl.AffiliateService;
import org.example.meetlearning.vo.affiliate.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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


    @Operation(summary = "代理商充值", operationId = "affiliateRecharge")
    @PostMapping(value = "v1/affiliate/recharge")
    public RespVo<String> affiliateRecharge(@RequestBody AffiliateRechargeReqVo reqVo) {
        return new RespVo<>("代理商充值成功");
    }

    @Operation(summary = "代理商充值记录列表", operationId = "affiliatePage")
    @PostMapping(value = "v1/affiliate/recharge/page")
    public RespVo<PageVo<AffiliateRechargeRecordRespVo>> affiliatePage(@RequestBody AffiliateRechargeQueryVo queryVo) {
        AffiliateRechargeRecordRespVo respVo = new AffiliateRechargeRecordRespVo();
        respVo.setRecordId("123123213");
        respVo.setAmount(new BigDecimal("2"));
        respVo.setBalanceAfter(new BigDecimal("3"));
        respVo.setRemark("备注");
        respVo.setCreator("123123123");
        respVo.setCreateName("张三");
        PageVo<AffiliateRechargeRecordRespVo> pageVo = PageVo.getNewPageVo(1, 10, 1, 1, List.of(respVo));
        return new RespVo<>(pageVo);
    }

}