package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.Textbook;

public interface TextbookMapper {

    int insert(Textbook record);

    Page<Textbook> selectPageByParams(Map<String, Object> params, Page<Textbook> page);

    List<Textbook> selectByParams(@Param("params") Map<String, Object> params);

    Textbook selectByRecordId(String recordId);

    Textbook selectByName(String name);

    int update(Textbook record);

    int deletedByRecordId(String recordId);
}