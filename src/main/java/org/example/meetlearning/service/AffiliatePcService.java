package org.example.meetlearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.AffiliateConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.AffiliateService;
import org.example.meetlearning.vo.affiliate.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class AffiliatePcService extends BasePcService {

    private final AffiliateService affiliateService;

    public RespVo<PageVo<AffiliateListPageRespVo>> affiliatePage(AffiliateQueryVo queryVo) {
        try {
            Page<Affiliate> page = affiliateService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
            PageVo<AffiliateListPageRespVo> pageVo = PageVo.map(page, AffiliateConverter.INSTANCE::toListResp);
            return new RespVo<>(pageVo);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, "查询失败,未知错误!");
        }
    }

    public RespVo<String> affiliateAdd(String userCode, String userName, AffiliateAddReqVo reqVo) {
        try {

            // 验证码校验
            emailVerify(reqVo.getEmail(), reqVo.getVerifyCode());

            // 保存代理商基础信息
            Affiliate affiliate = AffiliateConverter.INSTANCE.toCreateAffiliate(userCode, userName, reqVo);
            affiliateService.insertEntity(affiliate);

            // 创建登陆帐号
            addUser(userCode, userName, affiliate.getRecordId(), affiliate.getEmail(), reqVo.getPassword(),
                    RoleEnum.MANAGER, affiliate.getName(), affiliate.getEnName(), affiliate.getEmail());

            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>(null, false, "Addition failed,未知错误!");
        }
    }


    public RespVo<String> affiliateUpdate(String userCode, String userName, AffiliateUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
            Assert.notNull(affiliate, "为获取到对象信息 recordId" + reqVo.getRecordId());
            affiliate = AffiliateConverter.INSTANCE.toUpdateAffiliate(userCode, userName, affiliate, reqVo);
            affiliateService.insertEntity(affiliate);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>(null, false, "Addition failed,未知错误!");
        }
    }


    public RespVo<String> affiliateUpdateRemark(String userCode, String userName, AffiliateRemarkUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
            Assert.notNull(affiliate, "为获取到对象信息 recordId" + reqVo.getRecordId());
            affiliate.setRemark(reqVo.getRemark());
            affiliateService.insertEntity(affiliate);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>("Addition failed", false, ex.getMessage());
        }
    }


    public RespVo<List<SelectValueVo>> affiliateSelect() {
        try {
            List<SelectValueVo> selectValueVos = affiliateService.affiliateSelect();
            return new RespVo<>(selectValueVos);
        } catch (Exception ex) {
            log.error("查询失败", ex);
            return new RespVo<>(null, false, "查询失败,未知错误!");
        }
    }


    public RespVo<AffiliateDashboardRespVo> dashboard(String userCode) {
        try {
            return new RespVo<>(new AffiliateDashboardRespVo(new BigDecimal(12), new BigDecimal(13), new BigDecimal(14)));
        } catch (Exception ex) {
            log.error("Query failed", ex);
            return new RespVo<>(null, false, ex.getMessage());
        }
    }

}
