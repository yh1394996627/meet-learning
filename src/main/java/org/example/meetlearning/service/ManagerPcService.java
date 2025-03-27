package org.example.meetlearning.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.converter.UserFinanceConverter;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.manager.ManagerAmountRespVo;

import io.micrometer.common.util.StringUtils;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.service.impl.TokensLogService;
import org.example.meetlearning.service.impl.UserFinanceRecordService;
import org.example.meetlearning.vo.manager.ManagerFinanceStudentRecordRespVo;
import org.example.meetlearning.vo.manager.ManagerIncomeStatisticsQueryVo;
import org.example.meetlearning.vo.manager.ManagerIncomeStatisticsRespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ManagerPcService {

    private final UserFinanceRecordService userFinanceRecordService;

    private final TokensLogService tokensLogService;

    public ManagerIncomeStatisticsRespVo dashboard(String userCode, ManagerIncomeStatisticsQueryVo queryVo) {
        ManagerIncomeStatisticsRespVo respVo = new ManagerIncomeStatisticsRespVo();
        String day = queryVo.getDay();
        Assert.isTrue(StringUtils.isNotEmpty(day), "The day condition cannot be empty");
        List<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectByParamsGroup(queryVo.getParams(userCode));
        List<UserFinanceRecord> lastUserFinanceRecordList = userFinanceRecordService.selectByParamsGroup(queryVo.getLastParams(userCode));
        Map<String, UserFinanceRecord> lastMap = lastUserFinanceRecordList.stream().collect(Collectors.toMap(UserFinanceRecord::getCurrencyCode, Function.identity()));
        //总金额对比
        List<ManagerAmountRespVo> amountList = userFinanceRecordList.stream().map(record -> {
            ManagerAmountRespVo amountRespVo = new ManagerAmountRespVo();
            amountRespVo.setCurrencyCode(record.getCurrencyCode());
            amountRespVo.setCurrencyName(record.getCurrencyName());
            amountRespVo.setAmount(record.getPayAmount());
            amountRespVo.setRate(new BigDecimal("100"));
            if (lastMap.containsKey(amountRespVo.getCurrencyCode())) {
                BigDecimal lastAmount = BigDecimalUtil.nullOrOne(lastMap.get(amountRespVo.getCurrencyCode()).getPayAmount());
                amountRespVo.setRate(BigDecimalUtil.divide(record.getPayAmount(), lastAmount, 4).multiply(new BigDecimal(100)));
            }
            return amountRespVo;
        }).collect(Collectors.toList());
        respVo.setAmountList(amountList);

        Map<String, Object> params = queryVo.getParams(userCode);
        //学生充值记录
        params.put("userType", RoleEnum.STUDENT.name());
        List<UserFinanceRecord> recordList = userFinanceRecordService.selectDaByParams(params);

        respVo.setTransactions(recordList.size());
        BigDecimal balanceQty = recordList.stream().map(UserFinanceRecord::getBalanceQty).reduce(BigDecimal.ZERO, BigDecimal::add);
        respVo.setBalanceQty(balanceQty);
        List<UserFinanceRecord> studentFinanceRecordList = userFinanceRecordService.selectByParamsGroup(params);
        //学生金额信息
        List<ManagerAmountRespVo> studentAmountList = studentFinanceRecordList.stream().map(record -> {
            ManagerAmountRespVo amountRespVo = new ManagerAmountRespVo();
            amountRespVo.setCurrencyCode(record.getCurrencyCode());
            amountRespVo.setCurrencyName(record.getCurrencyName());
            amountRespVo.setAmount(record.getPayAmount());
            return amountRespVo;
        }).collect(Collectors.toList());
        respVo.setStudentAmountList(studentAmountList);
        return respVo;
    }


    public PageVo<ManagerFinanceStudentRecordRespVo> financeList(String userCode, ManagerIncomeStatisticsQueryVo queryVo) {
        Page<UserFinanceRecord> recordList = userFinanceRecordService.selectByParams(queryVo.getParams(userCode), queryVo.getPageRequest());
        return PageVo.map(recordList, UserFinanceConverter.INSTANCE::toManagerFinanceStudentRecordRespVo);
    }


}
