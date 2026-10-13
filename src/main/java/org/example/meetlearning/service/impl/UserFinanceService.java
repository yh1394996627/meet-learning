package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.dao.mapper.UserFinanceMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserFinanceService {

    private final UserFinanceMapper userFinanceMapper;

    /**
     * 批量插入
     */
    public int insertEntity(UserFinance record) {
        return userFinanceMapper.insertEntity(record);
    }

    /**
     * 更新
     */
    public void updateByEntity(UserFinance record) {
        userFinanceMapper.updateByEntity(record);
    }

    /**
     * 根据用户id查询
     */
    public UserFinance selectByUserId(String userId) {
        return userFinanceMapper.selectByUserId(userId);
    }

    /**
     * 根据用户id查询
     */
    public List<UserFinance> selectByUserIds(List<String> userIds) {
        if(CollectionUtils.isEmpty(userIds)){
            return new ArrayList<>();
        }
        return userFinanceMapper.selectByUserIds(userIds);
    }

    /**
     * 增量更新余额和消费总量（数据库层原子操作，避免并发丢失更新）
     */
    public int addBalanceAndSubConsumption(String userId, java.math.BigDecimal addBalance, java.math.BigDecimal subConsumption) {
        return userFinanceMapper.addBalanceAndSubConsumption(userId, addBalance, subConsumption);
    }

    /**
     * 根据用户id删除
     */
    public void deleteByUserId(String userId) {
        userFinanceMapper.deleteByUserId(userId);
    }




}
