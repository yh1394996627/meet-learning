package org.example.meetlearning.controller.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
public class AffiliateListController {

    @Operation(summary = "代理商列表", operationId = "affiliatePage")
    @PostMapping(value = "v1/affiliate/page")
    public RespVo<PageVo<AffiliateListPageRespVo>> affiliatePage(AffiliateQueryVo queryVo) {
        AffiliateListPageRespVo reqVo = new AffiliateListPageRespVo();
        reqVo.setRecordId(UUID.randomUUID().toString());
        reqVo.setName("11112222");
        reqVo.setEmail("11113333@qq.com");
        reqVo.setBalance(new BigDecimal("12"));
        reqVo.setCommissionRate(new BigDecimal("33"));
        reqVo.setRate(new BigDecimal("10"));
        reqVo.setStudentTotal(new BigDecimal("5"));
        reqVo.setCourseTotal(new BigDecimal("20"));
        reqVo.setRemark("123123123123123");
        PageVo<AffiliateListPageRespVo> pageVo = PageVo.getNewPageVo(1, 10, 1, 1, List.of(reqVo));
        return new RespVo<>(pageVo);
    }

    @Operation(summary = "新增代理商", operationId = "affiliateAdd")
    @PostMapping(value = "v1/affiliate/add")
    public RespVo<String> affiliateAdd(@RequestBody AffiliateAddReqVo reqVo) {
        return new RespVo<>("新增成功");
    }


    @Operation(summary = "修改代理商", operationId = "affiliateUpdate")
    @PostMapping(value = "v1/affiliate/update")
    public RespVo<String> affiliateUpdate(@RequestBody AffiliateUpdateReqVo reqVo) {
        return new RespVo<>("更新成功");
    }

    @Operation(summary = "修改代理商备注", operationId = "affiliateUpdateRemark")
    @PostMapping(value = "v1/affiliate/update/remark")
    public RespVo<String> affiliateUpdateRemark(@RequestBody AffiliateRemarkUpdateReqVo reqVo) {
        return new RespVo<>("更新备注成功");
    }


    @Operation(summary = "代理商充值", operationId = "affiliateRecharge")
    @PostMapping(value = "v1/affiliate/recharge")
    public RespVo<String> affiliateRecharge(@RequestBody AffiliateRechargeReqVo reqVo) {
        return new RespVo<>("代理商充值成功");
    }

    @Operation(summary = "代理商充值记录列表", operationId = "affiliatePage")
    @PostMapping(value = "v1/affiliate/page")
    public RespVo<PageVo<AffiliateRechargeRecordRespVo>> affiliatePage(AffiliateRechargeQueryVo queryVo) {
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