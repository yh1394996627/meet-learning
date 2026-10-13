package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.UserFinance;

public interface UserFinanceMapper {

    int deleteByUserId(String userId);

    int insertEntity(UserFinance record);

    UserFinance selectByUserId(String userId);

    List<UserFinance> selectByUserIds(List<String> userIds);

    int updateByEntity(UserFinance record);

    /**
     * 增量更新用户余额和消费总量（数据库层原子操作，避免并发丢失更新）
     * @param userId 用户ID
     * @param addBalance 余额加回数量
     * @param subConsumption 消费总量扣减数量
     */
    int addBalanceAndSubConsumption(@Param("userId") String userId,
                                    @Param("addBalance") java.math.BigDecimal addBalance,
                                    @Param("subConsumption") java.math.BigDecimal subConsumption);

}