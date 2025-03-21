package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.entity.UserFinance;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface UserFinanceConverter {

    UserFinanceConverter INSTANCE = Mappers.getMapper(UserFinanceConverter.class);

    default UserFinance toCreate(String userCode, String userName , User user) {
        UserFinance userFinance = new UserFinance();
        userFinance.setDeleted(false);
        userFinance.setCreator(userCode);
        userFinance.setCreateName(userName);
        userFinance.setCreateTime(new Date());
        userFinance.setRecordId(UUID.randomUUID().toString());
        userFinance.setUserId(user.getRecordId());
        userFinance.setBalanceQty(BigDecimal.ZERO);
        userFinance.setConsumptionQty(BigDecimal.ZERO);
        userFinance.setUserType(user.getType());
        userFinance.setExpirationTime(null);
        return userFinance;
    }





}
