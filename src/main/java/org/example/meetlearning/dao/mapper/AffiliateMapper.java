package org.example.meetlearning.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.vo.common.SelectValueVo;

import java.util.List;
import java.util.Map;

public interface AffiliateMapper {

    int insertEntity(Affiliate record);

    Affiliate selectByRecordId(String recordId);

    List<SelectValueVo> selectValueAll();

    Page<Affiliate> selectByParams(@Param("params")Map<String,Object> params, Page<Affiliate> page);

    int updateEntity(Affiliate record);

}