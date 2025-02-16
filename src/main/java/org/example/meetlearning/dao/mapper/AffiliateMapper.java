package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.Affiliate;

public interface AffiliateMapper {

    int insertSelective(Affiliate record);

    Affiliate selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Affiliate record);

}