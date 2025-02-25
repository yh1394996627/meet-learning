package org.example.meetlearning.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.Affiliate;

import java.util.Map;

public interface AffiliateMapper {

    int insertEntity(Affiliate record);

    Affiliate selectByRecordId(String recordId);

    Page<Affiliate> selectByParams(Map<String,Object> params, Page<Affiliate> page);

    int updateEntity(Affiliate record);

}