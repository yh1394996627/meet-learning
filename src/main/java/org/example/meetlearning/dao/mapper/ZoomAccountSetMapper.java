package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.ZoomAccountSet;

public interface ZoomAccountSetMapper {

    int insert(ZoomAccountSet record);

    List<ZoomAccountSet> selectByParams(Map<String, Object> params);

    List<ZoomAccountSet> selectActivation();

    ZoomAccountSet selectByRecordId(String recordId);

    ZoomAccountSet selectByAccountId(String recordId);

    ZoomAccountSet selectOneOrderByQty();

    int deletedByRecordId(String recordId);

    int updateEntity(ZoomAccountSet record);

}