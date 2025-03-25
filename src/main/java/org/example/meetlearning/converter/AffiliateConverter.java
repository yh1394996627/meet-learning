package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;


import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.vo.affiliate.AffiliateAddReqVo;
import org.example.meetlearning.vo.affiliate.AffiliateListPageRespVo;
import org.example.meetlearning.vo.affiliate.AffiliateUpdateReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AffiliateConverter {

    AffiliateConverter INSTANCE = Mappers.getMapper(AffiliateConverter.class);

    default AffiliateListPageRespVo toListResp(Affiliate affiliate) {
        AffiliateListPageRespVo respVo = new AffiliateListPageRespVo();
        respVo.setRecordId(affiliate.getRecordId());
        respVo.setName(affiliate.getName());
        respVo.setEmail(affiliate.getEmail());
        respVo.setBalance(affiliate.getBalance());
        respVo.setCommissionRate(affiliate.getCommissionRate());
        respVo.setRate(affiliate.getRate());
        respVo.setStudentTotal(affiliate.getStudentTotal());
        respVo.setCourseTotal(affiliate.getCourseTotal());
        respVo.setRemark(affiliate.getRemark());
        return respVo;
    }


    default Affiliate toCreateAffiliate(String userCode, String userName, AffiliateAddReqVo reqVo) {
        Affiliate affiliate = new Affiliate();
        affiliate.setCreator(userCode);
        affiliate.setCreateName(userName);
        affiliate.setCreateTime(new Date());
        affiliate.setDeleted(false);
        affiliate.setEmail(reqVo.getEmail());
        affiliate.setEnName(reqVo.getEnName());
        affiliate.setName(reqVo.getEnName());
        affiliate.setRemark(reqVo.getRemark());
        affiliate.setRecommenderId(reqVo.getRecommenderId());
        affiliate.setRecommender(reqVo.getRecommender());
        return affiliate;
    }

    default Affiliate toUpdateAffiliate(String userCode, String userName, Affiliate affiliate, AffiliateUpdateReqVo reqVo) {
        affiliate.setUpdator(userCode);
        affiliate.setUpdateName(userName);
        affiliate.setUpdateTime(new Date());
        affiliate.setCommissionRate(reqVo.getCommissionRate());
        affiliate.setRecommenderId(reqVo.getRecommenderId());
        affiliate.setRecommender(reqVo.getRecommender());
        return affiliate;
    }


}
