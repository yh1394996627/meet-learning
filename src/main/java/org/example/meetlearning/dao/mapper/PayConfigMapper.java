package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.PayConfig;

public interface PayConfigMapper {

    int deleteByRecordId(String recordId);

    int insert(PayConfig record);

    List<PayConfig> selectByCurrency(String currencyCode);

    PayConfig selectByRecordId(String recordId);

    int updateEntity(PayConfig record);
}