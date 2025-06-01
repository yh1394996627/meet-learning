package org.example.meetlearning.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.UserFinanceRecord;

public interface UserFinanceRecordMapper {

    int insertEntity(UserFinanceRecord record);

    List<UserFinanceRecord> selectByUserId(String userId);

    Page<UserFinanceRecord> selectByParams(Map<String, Object> params, Page<UserFinanceRecord> page);

    List<UserFinanceRecord> selectByParamsCurrencyGroup(@Param("params") Map<String, Object> params);

    List<UserFinanceRecord> selectDaByParams(Map<String, Object> params);

    List<UserFinanceRecord> selectDateGroupByUserIds(List<String> userIds);

    int updateByEntity(UserFinanceRecord record);

    List<UserFinanceRecord> selectByLtDate(Date expirationTime);

}