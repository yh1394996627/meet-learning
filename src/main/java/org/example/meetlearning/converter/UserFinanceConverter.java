package org.example.meetlearning.converter;

import cn.hutool.core.date.DateUtil;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.TokenContentEnum;
import org.example.meetlearning.vo.manager.ManagerFinanceStudentRecordRespVo;
import org.example.meetlearning.vo.user.UserStudentPayRecordRespVo;
import org.example.meetlearning.vo.user.UserPayReqVo;
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


    default UserFinanceRecord toCreateRecord(String userCode, String userName, UserPayReqVo reqVo, User user, String affiliateId) {
        UserFinanceRecord userFinanceRecord = new UserFinanceRecord();
        userFinanceRecord.setRecordId(UUID.randomUUID().toString());
        userFinanceRecord.setDeleted(false);
        userFinanceRecord.setCreator(userCode);
        userFinanceRecord.setCreateName(userName);
        userFinanceRecord.setCreateTime(new Date());
        userFinanceRecord.setUserId(reqVo.getUserId());
        userFinanceRecord.setQuantity(reqVo.getQuantity());
        userFinanceRecord.setUsedQty(BigDecimal.ZERO);
        userFinanceRecord.setCanQty(reqVo.getQuantity());
        userFinanceRecord.setUserType(user.getType());
        userFinanceRecord.setUserName(user.getName());
        userFinanceRecord.setUserEmail(user.getEmail());
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
        userFinanceRecord.setAffiliateId(affiliateId);
        return userFinanceRecord;
    }


    default UserFinanceRecord toWeChatRechargeRecord(User user, UserFinance userFinance, PayConfig payConfig, RechargeOrder rechargeOrder, BigDecimal quantity, Date expirationTime) {
        UserFinanceRecord userFinanceRecord = new UserFinanceRecord();
        userFinanceRecord.setDeleted(false);
        userFinanceRecord.setRecordId(UUID.randomUUID().toString());
        userFinanceRecord.setCreator(user.getRecordId());
        userFinanceRecord.setCreateName(user.getName());
        userFinanceRecord.setCreateTime(new Date());
        userFinanceRecord.setUserId(userFinance.getUserId());
        userFinanceRecord.setQuantity(quantity);
        userFinanceRecord.setUsedQty(BigDecimal.ZERO);
        userFinanceRecord.setCanQty(quantity);
        userFinanceRecord.setUserType(userFinance.getUserType());
        userFinanceRecord.setUserName(user.getName());
        userFinanceRecord.setUserEmail(user.getEmail());
        if (expirationTime != null) {
            userFinanceRecord.setExpirationTime(expirationTime);
        }
        userFinanceRecord.setCurrencyCode(payConfig.getCurrencyCode());
        userFinanceRecord.setCurrencyName(payConfig.getCurrencyName());
        userFinanceRecord.setPayAmount(rechargeOrder.getAmount());
        userFinanceRecord.setPaymentId("WeChat");
        userFinanceRecord.setPaymentName("WeChat");
        userFinanceRecord.setRemark(TokenContentEnum.WECHAT_RECHARGE.getEnContent());
        userFinanceRecord.setDiscountRate(BigDecimal.ONE);
        userFinanceRecord.setAffiliateId(user.getManagerId());
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

    default ManagerFinanceStudentRecordRespVo toManagerFinanceStudentRecordRespVo(UserFinanceRecord record) {
        ManagerFinanceStudentRecordRespVo respVo = new ManagerFinanceStudentRecordRespVo();
        respVo.setRecordId(UUID.randomUUID().toString());
        respVo.setUserEmail(record.getUserEmail());
        respVo.setUserName(record.getUserName());
        respVo.setCurrencyCode(record.getCurrencyCode());
        respVo.setCurrencyName(record.getCurrencyName());
        respVo.setPaymentId(record.getPaymentId());
        respVo.setPaymentName(record.getPaymentName());
        respVo.setNote(record.getRemark());
        respVo.setCreator(record.getCreator());
        respVo.setCreateName(record.getCreateName());
        respVo.setCreateTime(record.getCreateTime());
        return respVo;
    }

}
