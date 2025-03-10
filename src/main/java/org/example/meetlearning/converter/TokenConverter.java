package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.TokensLog;
import org.example.meetlearning.enums.CurrencyEnum;
import org.example.meetlearning.vo.token.TokensLogAddReqVo;
import org.example.meetlearning.vo.token.TokensLogListRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface TokenConverter {

    TokenConverter INSTANCE = Mappers.getMapper(TokenConverter.class);

    default TokensLogListRespVo toListVo(String userCode, String userName, TokensLog tokensLog) {
        TokensLogListRespVo respVo = new TokensLogListRespVo();
        respVo.setRecordId(tokensLog.getRecordId());
        respVo.setQuantity(tokensLog.getQuantity());
        respVo.setRemark(tokensLog.getNote());
        respVo.setBalance(tokensLog.getBalance());
        respVo.setCreateTime(new Date());
        respVo.setCreator(userCode);
        respVo.setCreateName(userName);
        return respVo;
    }


    default TokensLog toCreateToken(String userCode, String userName, TokensLogAddReqVo tokensLogAddReqVo) {
        TokensLog tokensLog = new TokensLog();
        tokensLog.setDeleted(false);
        tokensLog.setUserId(tokensLogAddReqVo.getUserId());
        tokensLog.setCreator(userCode);
        tokensLog.setCreateName(userName);
        tokensLog.setCreateTime(new Date());
        tokensLog.setRecordId(UUID.randomUUID().toString());
        tokensLog.setQuantity(tokensLogAddReqVo.getQuantity());
        tokensLog.setBalance(new BigDecimal("0"));
        tokensLog.setAmount(tokensLogAddReqVo.getAmount());
        CurrencyEnum currency = CurrencyEnum.getBySymbol(tokensLogAddReqVo.getCurrencySymbol());
        assert currency != null;
        tokensLog.setCurrencyCode(currency.name());
        tokensLog.setCurrencyName(currency.getName());
        tokensLog.setCurrencySymbol(currency.getSymbol());
        tokensLog.setNote(tokensLogAddReqVo.getCurrencyName());
        return tokensLog;
    }


}
