package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.ManagerPcService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.manager.ManagerFinanceStudentRecordRespVo;
import org.example.meetlearning.vo.manager.ManagerIncomeStatisticsQueryVo;
import org.example.meetlearning.vo.manager.ManagerIncomeStatisticsRespVo;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "管理员接口")
@RestController
@Slf4j
@AllArgsConstructor
public class ManagerController implements BaseController {

    private final ManagerPcService managerPcService;

    @Operation(summary = "管理员表盘 - 金额比对", operationId = "dashboard")
    @PostMapping(value = "v1/manager/dashboard")
    public RespVo<ManagerIncomeStatisticsRespVo> dashboard(@RequestBody ManagerIncomeStatisticsQueryVo queryVo) {
        return new RespVo<>(managerPcService.dashboard(getUserCode(), queryVo));
    }

    @Operation(summary = "学生充值记录查询", operationId = "financeList")
    @PostMapping(value = "v1/manager/finance/list")
    public RespVo<PageVo<ManagerFinanceStudentRecordRespVo>> financeList(@RequestBody ManagerIncomeStatisticsQueryVo queryVo) {
        return new RespVo<>(managerPcService.financeList(getUserCode(), queryVo));
    }

}
