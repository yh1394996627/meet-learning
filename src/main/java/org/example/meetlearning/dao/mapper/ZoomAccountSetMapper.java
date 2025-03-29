package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.ZoomAccountSet;

public interface ZoomAccountSetMapper {

    int insert(ZoomAccountSet record);

    List<ZoomAccountSet> selectByParams(Map<String,Object> params);

    ZoomAccountSet selectByRecordId(String recordId);

    int deletedByRecordId(String recordId);

    int updateEntity(ZoomAccountSet record);

}