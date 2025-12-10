package org.example.meetlearning.service;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.TokenConverter;
import org.example.meetlearning.converter.UserFinanceConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.LanguageContextEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.token.TokensLogAddReqVo;
import org.example.meetlearning.vo.token.TokensLogListRespVo;
import org.example.meetlearning.vo.token.TokensLogQueryVo;
import org.example.meetlearning.vo.user.UserPayReqVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
@Slf4j
public class TokensLogPcService extends BasePcService {

    private final TokensLogService tokensLogService;

    private final BaseConfigService baseConfigService;

    private final UserService userService;

    private final UserFinanceService userFinanceService;

    private final UserFinanceRecordService userFinanceRecordService;

    public RespVo<PageVo<TokensLogListRespVo>> tokensLogPage(String userCode, String userName, TokensLogQueryVo queryVo) {
        Map<String, Object> params = new HashMap<>();
        String userId = StringUtils.isNotEmpty(queryVo.getRecordId()) ? queryVo.getRecordId() : userCode;
        params.put("userId", userId);
        Page<TokensLog> page = tokensLogService.selectPageByParams(params, queryVo.getPageRequest());
        PageVo<TokensLogListRespVo> pageVO = PageVo.map(page, list -> TokenConverter.INSTANCE.toListVo(userCode, userName, list));
        return new RespVo<>(pageVO);
    }


    public RespVo<String> addTokensLog(String userCode, String userName, TokensLogAddReqVo tokensLogAddReqVo, Boolean isDeletedCallBack) {
        tokensLogAddReqVo.setIsDeletedCallBack(isDeletedCallBack);
        String userId = StringUtils.isNotEmpty(tokensLogAddReqVo.getRecordId()) ? tokensLogAddReqVo.getRecordId() : userCode;
        Assert.isTrue(StringUtils.isNotEmpty(tokensLogAddReqVo.getRecordId()), "user is not null");
        financeTokenLogs(userCode, userName, userId, tokensLogAddReqVo);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    @Transactional(rollbackFor = Exception.class)
    public void financeTokenLogs(String userCode, String userName, String userId, TokensLogAddReqVo reqVo) {
        User user = userService.selectByRecordId(userId);
        Assert.notNull(user, getHint(LanguageContextEnum.USER_NOTNULL));
        UserFinance userFinance = userFinanceService.selectByUserId(userId);
        Assert.notNull(userFinance, getHint(LanguageContextEnum.USER_FINANCE_NOTNULL));
        BigDecimal balanceQty = userFinance.getBalanceQty();
        BigDecimal balance = BigDecimalUtil.add(balanceQty, reqVo.getQuantity());
        Assert.isTrue(BigDecimalUtil.gteZero(balance), getHint(LanguageContextEnum.INSUFFICIENT_BALANCE));
        //更新 userFinance
        userFinance.setBalanceQty(balance);
        //新增用户课时币记录 userFinanceRecord
        UserFinanceRecord userFinanceRecord = UserFinanceConverter.INSTANCE.toAffCreateRecord(userCode, userName, reqVo, user, null);
        userFinanceRecord.setUserId(userId);
        userFinanceRecord.setBalanceQty(userFinance.getBalanceQty().add(reqVo.getQuantity()));
        userFinanceRecordService.insertEntity(userFinanceRecord);
        //添加记录课时币记录
        TokensLog tokensLog = TokenConverter.INSTANCE.toCreateTokenByFinanceRecord(userCode, userName, userFinance, user, reqVo.getQuantity(), reqVo.getAmount(), reqVo.getRemark());
        tokensLog.setUserId(userId);
        if (!StringUtils.isEmpty(reqVo.getCurrencyCode())) {
            BaseConfig baseConfig = baseConfigService.selectByCode(reqVo.getCurrencyCode());
            Assert.notNull(baseConfig, "Configuration information not obtained record:【" + reqVo.getCurrencyCode() + "】");
            tokensLog.setCurrencyCode(baseConfig.getCode());
            tokensLog.setCurrencyName(baseConfig.getName());
        }
        if (BooleanUtils.isTrue(reqVo.getIsDeletedCallBack())) {
            List<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectByUserId(userId);


        }
        tokensLogService.insertEntity(tokensLog);
        userFinanceService.updateByEntity(userFinance);
    }


    public void repairTokensLog(List<String> userIds) {
        for (String userId : userIds) {
            UserFinance userFinance = userFinanceService.selectByUserId(userId);
            List<UserFinanceRecord> records = userFinanceRecordService.selectByAllUserId(userId);
            records = records.stream().sorted(Comparator.comparing(UserFinanceRecord::getDeleted).reversed()).toList();
            BigDecimal totalQty = records.stream().map(UserFinanceRecord::getUsedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            for (UserFinanceRecord record : records) {
                record.setCanQty(record.getQuantity());
                record.setUsedQty(BigDecimal.ZERO);
                if (BigDecimalUtil.gtZero(totalQty)) {
                    BigDecimal canQty = record.getCanQty();
                    BigDecimal subQty = totalQty.min(canQty);
                    record.setUsedQty(subQty);
                    record.setCanQty(canQty.subtract(subQty));
                    totalQty = totalQty.subtract(subQty);
                }
                userFinanceRecordService.updateByEntity(record);
            }
            BigDecimal baQty = records.stream().filter(f -> !f.getDeleted()).map(UserFinanceRecord::getCanQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal baQty1 = records.stream().map(UserFinanceRecord::getUsedQty).reduce(BigDecimal.ZERO, BigDecimal::add);
            userFinance.setBalanceQty(baQty);
            userFinance.setConsumptionQty(baQty1);
            userFinanceService.updateByEntity(userFinance);
        }
    }
}
