package org.example.meetlearning.converter;

import cn.hutool.core.date.DateUtil;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.vo.user.UserStudentPayRecordRespVo;
import org.example.meetlearning.vo.user.UserStudentPayReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface UserFinanceConverter {

    UserFinanceConverter INSTANCE = Mappers.getMapper(UserFinanceConverter.class);

    default UserFinance toCreate(String userCode, String userName, User user) {
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
        userFinance.setVersion(0L);
        return userFinance;
    }


    default UserFinanceRecord toCreateRecord(String userCode, String userName, UserStudentPayReqVo reqVo) {
        UserFinanceRecord userFinanceRecord = new UserFinanceRecord();
        userFinanceRecord.setDeleted(false);
        userFinanceRecord.setCreator(userCode);
        userFinanceRecord.setCreateName(userName);
        userFinanceRecord.setCreateTime(new Date());
        userFinanceRecord.setUserId(reqVo.getUserId());
        userFinanceRecord.setQuantity(reqVo.getQuantity());
        userFinanceRecord.setUsedQty(BigDecimal.ZERO);
        userFinanceRecord.setCanQty(reqVo.getQuantity());
        userFinanceRecord.setUserType("");
        if (StringUtils.isNotEmpty(reqVo.getExpirationTime())) {
            userFinanceRecord.setExpirationTime(DateUtil.parse(reqVo.getExpirationTime(), "yyyy-MM-dd"));
        }
        userFinanceRecord.setCurrencyCode(reqVo.getCurrencyCode());
        userFinanceRecord.setCurrencyName(reqVo.getCurrencyName());
        userFinanceRecord.setPayAmount(reqVo.getPayAmount());
        userFinanceRecord.setPaymentId(reqVo.getPaymentId());
        userFinanceRecord.setPaymentName(reqVo.getPaymentName());
        userFinanceRecord.setRemark(reqVo.getRemark());
        userFinanceRecord.setDiscountRate(reqVo.getDiscountRate());
        return userFinanceRecord;
    }


    default UserStudentPayRecordRespVo toUserStudentPayRecordRespVo(UserFinanceRecord record) {
        UserStudentPayRecordRespVo respVo = new UserStudentPayRecordRespVo();
        respVo.setUserId(record.getUserId());
        respVo.setRecordId(UUID.randomUUID().toString());
        respVo.setQuantity(record.getQuantity());
        respVo.setUsedQty(record.getUsedQty());
        respVo.setCanQty(record.getCanQty());
        respVo.setExpirationTime(record.getExpirationTime());
        return respVo;
    }

}
