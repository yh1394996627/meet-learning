package org.example.meetlearning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.dao.mapper.UserFinanceRecordMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class UserFinanceRecordService {

    private final UserFinanceRecordMapper userFinanceRecordMapper;

    /**
     * 批量插入
     */
    public int insertEntity(UserFinanceRecord record) {
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
    public List<UserFinanceRecord> selectByUserId(String userId) {
        return userFinanceRecordMapper.selectByUserId(userId);
    }

    /**
     * 分页查询
     */
    public Page<UserFinanceRecord> selectByParams(Map<String, Object> params, Page<UserFinanceRecord> page) {
        return userFinanceRecordMapper.selectByParams(params, page);
    }

    /**
     * 币种分组查询
     */
    public List<UserFinanceRecord> selectByParamsGroup(Map<String, Object> params) {
        return userFinanceRecordMapper.selectByParamsCurrencyGroup(params);
    }

    public List<UserFinanceRecord> selectDaByParams(Map<String, Object> params) {
        return userFinanceRecordMapper.selectDaByParams(params);
    }

    public List<UserFinanceRecord> selectDateGroupByUserIds(List<String> userIds) {
        return userFinanceRecordMapper.selectDateGroupByUserIds(userIds);
    }

    public List<UserFinanceRecord> selectByLtDate(Date expirationTime) {
        return userFinanceRecordMapper.selectByLtDate(expirationTime);
    }

}
