package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.User;

public interface UserMapper {

    int insertEntity(User record);

    User selectByRecordId(String recordId);

    User selectByLogin(String accountCode,String password);

    User selectByAccountCode(String accountCode);

    int updateEntity(User record);

}