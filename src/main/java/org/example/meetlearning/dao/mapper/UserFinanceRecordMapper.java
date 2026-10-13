package org.example.meetlearning.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.UserFinanceRecord;

public interface UserFinanceRecordMapper {

    int insertEntity(UserFinanceRecord record);

    UserFinanceRecord selectById(Long id);

    List<UserFinanceRecord> selectByUserId(String userId);

    List<UserFinanceRecord> selectByAllUserId(String userId);

    List<UserFinanceRecord> selectValidByUserId(String userId);

    Page<UserFinanceRecord> selectByParams(@Param("params")Map<String, Object> params, Page<UserFinanceRecord> page);

    List<UserFinanceRecord> selectByParamsCurrencyGroup(@Param("params") Map<String, Object> params);

    List<UserFinanceRecord> selectDaByParams(@Param("params")Map<String, Object> params);

    List<UserFinanceRecord> selectDateGroupByUserIds(List<String> userIds);

    int updateByEntity(UserFinanceRecord record);

    /**
     * 增量回滚 record 的 canQty 和 usedQty（数据库层原子操作，避免并发丢失更新）
     * canQty += addCanQty, usedQty -= subUsedQty
     */
    int rollbackQtyById(@Param("id") Long id,
                        @Param("addCanQty") java.math.BigDecimal addCanQty,
                        @Param("subUsedQty") java.math.BigDecimal subUsedQty);

    /**
     * 增量更新 canQty（数据库层原子操作，避免并发丢失更新）
     * canQty += addCanQty
     */
    int addCanQtyById(@Param("id") Long id,
                      @Param("addCanQty") java.math.BigDecimal addCanQty);

    List<UserFinanceRecord> selectByLtDate(Date expirationTime);

}