package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.dao.mapper.UserFinanceRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserFinanceRecordService {

    private final UserFinanceRecordMapper userFinanceRecordMapper;

    /**
     * 批量插入
     */
    public int insertBatch(UserFinanceRecord record) {
        return userFinanceRecordMapper.insertEntity(record);
    }
    /**
     * 更新
     */
    public int updateByEntity(UserFinanceRecord record) {
        return userFinanceRecordMapper.updateByEntity(record);
    }

    /**
     * 根据用户id查询
     */
    public List<UserFinanceRecord> selectByRecordId(String userId) {
        return userFinanceRecordMapper.selectByUserId(userId);
    }

}
