package org.example.meetlearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.AffiliateConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.service.impl.AffiliateService;
import org.example.meetlearning.vo.affiliate.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

@Service
@AllArgsConstructor
@Slf4j
public class AffiliatePcService {

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
            Affiliate affiliate = AffiliateConverter.INSTANCE.toCreateAffiliate(userCode, userName, reqVo);
            affiliateService.insertEntity(affiliate);
            return new RespVo<>("新增成功");
        } catch (Exception ex) {
            log.error("新增失败", ex);
            return new RespVo<>(null, false, "新增失败,未知错误!");
        }
    }


    public RespVo<String> affiliateUpdate(String userCode, String userName, AffiliateUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
            Assert.notNull(affiliate, "为获取到对象信息 recordId" + reqVo.getRecordId());
            affiliate = AffiliateConverter.INSTANCE.toUpdateAffiliate(userCode, userName, affiliate, reqVo);
            affiliateService.insertEntity(affiliate);
            return new RespVo<>("新增成功");
        } catch (Exception ex) {
            log.error("新增失败", ex);
            return new RespVo<>(null, false, "新增失败,未知错误!");
        }
    }


    public RespVo<String> affiliateUpdateRemark(String userCode, String userName, AffiliateRemarkUpdateReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId不能为空");
            Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
            Assert.notNull(affiliate, "为获取到对象信息 recordId" + reqVo.getRecordId());
            affiliate.setRemark(reqVo.getRemark());
            affiliateService.insertEntity(affiliate);
            return new RespVo<>("新增成功");
        } catch (Exception ex) {
            log.error("新增失败", ex);
            return new RespVo<>(null, false, "新增失败,未知错误!");
        }
    }


}
