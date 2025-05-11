package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.mapper.UserMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService {

    private final UserMapper userMapper;

    /**
     * 插入用户
     *
     */
    public void insertEntity(User user) {
        userMapper.insertEntity(user);
    }

    /**
     * 根据recordId查询用户
     *
     * @return 用户信息
     */
    public User selectByRecordId(String recordId) {
        return userMapper.selectByRecordId(recordId);
    }

    /**
     * 根据账号密码查询用户
     *
     * @return 用户信息
     */
    public User selectByLogin(String accountCode, String password) {
        return userMapper.selectByLogin(accountCode, password);
    }

    /**
     * 根据账号密码查询指定角色用户
     *
     * @return 用户信息
     */
    public User selectByRoleLogin(String accountCode, String password, String type) {
        return userMapper.selectByRoleLogin(accountCode, password, type);
    }

    /**
     * 更新用户
     */
    public int updateEntity(User user) {
        return userMapper.updateEntity(user);
    }


    /**
     * 根据账号查询用户
     *
     * @return 用户信息
     */
    public User selectByAccountCode(String accountCode) {
        return userMapper.selectByAccountCode(accountCode);
    }


    /**
     * 根据recordId删除用户
     *
     * @return 删除条数
     */
    public int deleteByRecordId(String recordId) {
        return userMapper.deleteByRecordId(recordId);
    }

}
