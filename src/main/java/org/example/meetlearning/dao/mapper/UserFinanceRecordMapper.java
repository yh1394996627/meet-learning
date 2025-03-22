package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.UserFinanceRecord;

public interface UserFinanceRecordMapper {

    int insertEntity(UserFinanceRecord record);

    List<UserFinanceRecord> selectByUserId(String userId);

    Page<UserFinanceRecord> selectByParams(Map<String, Object> params, Page<UserFinanceRecord> page);

    int updateByEntity(UserFinanceRecord record);

}