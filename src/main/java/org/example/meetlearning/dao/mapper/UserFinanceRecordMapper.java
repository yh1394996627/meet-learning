package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.example.meetlearning.dao.entity.UserFinanceRecord;

public interface UserFinanceRecordMapper {

    int insertEntity(UserFinanceRecord record);

    List<UserFinanceRecord> selectByUserId(String userId);

    int updateByEntity(UserFinanceRecord record);

}