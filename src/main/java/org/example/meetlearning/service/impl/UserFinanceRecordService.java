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
     * 根据id查询
     */
    public UserFinanceRecord selectById(Long id) {
        return userFinanceRecordMapper.selectById(id);
    }

    /**
     * 更新
     */
    public int updateByEntity(UserFinanceRecord record) {
        return userFinanceRecordMapper.updateByEntity(record);
    }

    /**
     * 增量回滚 canQty 和 usedQty（数据库原子操作，where 里带 used_qty >= subUsedQty 校验）
     * @return 受影响行数，0 表示 record 不存在或 usedQty 不足
     */
    public int rollbackQtyById(Long id, java.math.BigDecimal addCanQty, java.math.BigDecimal subUsedQty) {
        return userFinanceRecordMapper.rollbackQtyById(id, addCanQty, subUsedQty);
    }

    /**
     * 增量更新 canQty（数据库原子操作，避免并发丢失更新）
     */
    public int addCanQtyById(Long id, java.math.BigDecimal addCanQty) {
        return userFinanceRecordMapper.addCanQtyById(id, addCanQty);
    }

    /**
     * 根据用户id查询
     */
    public List<UserFinanceRecord> selectByUserId(String userId) {
        return userFinanceRecordMapper.selectByUserId(userId);
    }

    /**
     * 根据用户id查询
     */
    public List<UserFinanceRecord> selectByAllUserId(String userId) {
        return userFinanceRecordMapper.selectByAllUserId(userId);
    }

    /**
     * 根据用户id查询生效且可用数量>0的record
     */
    public List<UserFinanceRecord> selectValidByUserId(String userId) {
        return userFinanceRecordMapper.selectValidByUserId(userId);
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
