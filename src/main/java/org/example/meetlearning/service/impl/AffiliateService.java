package org.example.meetlearning.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.mapper.AffiliateMapper;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@AllArgsConstructor
public class AffiliateService {

    private final AffiliateMapper affiliateMapper;


    /**
     * 新增
     */
    public int insertEntity(Affiliate affiliate) {
        return affiliateMapper.insertEntity(affiliate);
    }

    /**
     * 修改
     */
    public int updateEntity(Affiliate affiliate) {
        return affiliateMapper.updateEntity(affiliate);
    }

    /**
     * 根据ID查询
     */
    public Affiliate findByRecordId(String recordId) {
        return affiliateMapper.selectByRecordId(recordId);
    }

    /**
     * 根据条件查询
     * @return 代理商分页
     * @param params 条件
     */
    public Page<Affiliate> findPageByParams(Map<String, Object> params, Page<Affiliate> page) {
        return affiliateMapper.selectByParams(params, page);
    }

}
