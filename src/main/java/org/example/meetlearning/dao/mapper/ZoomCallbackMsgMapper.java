package org.example.meetlearning.dao.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.ZoomCallbackMsg;

public interface ZoomCallbackMsgMapper {

    int deleteByRecordId(String recordId);

    int insert(ZoomCallbackMsg record);

    ZoomCallbackMsg selectByRecordId(String recordId);

    List<ZoomCallbackMsg> selectByRecordIds(List<String> recordIds);

    List<ZoomCallbackMsg> selectAll();

    int updateEntity(ZoomCallbackMsg record);

}