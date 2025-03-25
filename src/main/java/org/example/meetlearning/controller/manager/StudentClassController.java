package org.example.meetlearning.controller.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.StudentClassPcService;
import org.example.meetlearning.vo.classes.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "学生预约课程接口")
@RestController
@Slf4j
@AllArgsConstructor
public class StudentClassController implements BaseController {

    private final StudentClassPcService studentClassPcService;

    @Operation(summary = "学生预约记录列表", operationId = "studentClassPage")
    @PostMapping(value = "v1/student/class/page")
    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(@RequestBody StudentClassQueryVo queryVo) {
        return studentClassPcService.studentClassPage(queryVo);
    }

    @Operation(summary = "新增学生预约", operationId = "studentClassAdd")
    @PostMapping(value = "v1/student/class/add")
    public RespVo<String> studentClassAdd(@RequestBody StudentClassAddReqVo reqVo) {
        return studentClassPcService.studentClassAdd(getUserCode(), getUserName(), reqVo);
    }

    @Operation(summary = "选择课时币接口", operationId = "classCoinList")
    @PostMapping(value = "v1/student/class/coin/list")
    public RespVo<List<SelectValueVo>> classCoinList() {
        return studentClassPcService.classCoinList();
    }

    @Operation(summary = "选择时间段接口", operationId = "classTimeList")
    @PostMapping(value = "v1/student/class/time/list")
    public RespVo<List<SelectValueVo>> classTimeList(@RequestBody StudentClassCommonQueryVo queryVo) {
        return studentClassPcService.classTeacherList(queryVo);
    }

    @Operation(summary = "选择老师接口", operationId = "classTeacherList")
    @PostMapping(value = "v1/student/class/teacher/list")
    public RespVo<List<SelectValueVo>> classTeacherList(@RequestBody StudentClassCommonQueryVo queryVo) {
        return studentClassPcService.classTeacherList(queryVo);
    }

    @Operation(summary = "统计接口", operationId = "classTotalList")
    @PostMapping(value = "v1/student/class/total")
    public RespVo<StudentClassTotalRespVo> classTotalList(@RequestBody StudentClassQueryVo queryVo) {
        return studentClassPcService.classTotalList(queryVo);
    }


}
