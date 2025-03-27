package org.example.meetlearning.vo.manager;

import io.micrometer.common.util.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.vo.common.PageRequestQuery;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@Data
public class ManagerIncomeStatisticsQueryVo extends PageRequestQuery<UserFinanceRecord> {

    @Schema(name = "income", description = "类型  1:天  2:月")
    private Integer income;

    @Schema(name = "day", description = "按天查询需要传 例子:2025-01-01")
    private String day;

    @Schema(name = "yearMonth", description = "按月查询需要传 例子:2025-01")
    private String yearMonth;

    @Schema(hidden = true)
    public Map<String, Object> getParams(String userCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("creator", userCode);
        params.put("userType", RoleEnum.STUDENT.name());
        if (income != null) {
            params.put("income", income);
        }
        if (StringUtils.isNotEmpty(day)) {
            params.put("incomeDate", day);
        }
        if (StringUtils.isNotEmpty(yearMonth)) {
            params.put("incomeDate", yearMonth);
        }
        return params;
    }

    @Schema(hidden = true)
    public Map<String, Object> getLastParams(String userCode) {
        Map<String, Object> params = new HashMap<>();
        params.put("creator", userCode);
        if (income != null) {
            params.put("income", income);
        }
        if (StringUtils.isNotEmpty(day)) {
            params.put("incomeDate", getYesterday(day));
        }
        if (StringUtils.isNotEmpty(yearMonth)) {
            params.put("incomeDate", getLastMonth(yearMonth));
        }
        return params;
    }


    public static String getLastMonth(String yyyyMM) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        YearMonth yearMonth = YearMonth.parse(yyyyMM, formatter);
        YearMonth lastMonth = yearMonth.minusMonths(1);
        return lastMonth.format(formatter);
    }

    public static String getYesterday(String yyyyMMdd) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(yyyyMMdd, formatter);
        LocalDate yesterdayDate = date.minusDays(1);
        return yesterdayDate.format(formatter);
    }
}
