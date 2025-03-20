package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.example.meetlearning.dao.entity.UserFinance;

public interface UserFinanceMapper {

    int deleteByUserId(Long id);

    int insertEntity(UserFinance record);

    UserFinance selectByUserId(String userId);

    List<UserFinance> selectByUserIds(List<String> userIds);

    int updateByEntity(UserFinance record);

}