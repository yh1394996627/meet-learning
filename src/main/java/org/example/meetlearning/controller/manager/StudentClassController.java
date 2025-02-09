package org.example.meetlearning.controller.manager;

import java.math.BigDecimal;
import java.util.Date;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
public class StudentClassController {


    @Operation(summary = "学生预约记录列表", operationId = "studentClassPage")
    @PostMapping(value = "v1/student/class/page")
    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(StudentClassQueryVo queryVo) {
        StudentClassListRespVo respVo = new StudentClassListRespVo();
        respVo.setRecordId(UUID.randomUUID().toString());
        respVo.setStudentId(333L);
        respVo.setStudentName("张三");
        respVo.setStudentLanguage("英语");
        respVo.setTeacherId("222");
        respVo.setTeacherName("李四");
        respVo.setTeacherLanguage("英语");
        respVo.setCourseId("123213");
        respVo.setCourseName("云烟");
        respVo.setCourseTime(new Date());
        respVo.setCourseType("SINGLE");
        respVo.setCourseLongTime(new BigDecimal("60"));
        respVo.setTeacherCourseStatusContent("未完成");
        respVo.setStudentCourseStatusContent("已完成 ");
        respVo.setCourseVideoUrl("https://view.duofenpai.com/customerTrans/f70d662ddcc264e3f9635a6ad9055862/2f44bbe4-192e57e4481-0005-ae76-cb3-11458.mp4");
        respVo.setIsCourseVideoExpired(false);
        respVo.setAffiliateId("11222333");
        respVo.setAffiliateName("代理商1");
        respVo.setStudentConsumption(new BigDecimal("12"));
        respVo.setStudentBalance(new BigDecimal("88"));
        respVo.setEfficientDate(new Date());
        return new RespVo<>(PageVo.getNewPageVo(1, 10, 1, 1, List.of(respVo)));
    }


    @Operation(summary = "新增学生预约", operationId = "studentClassAdd")
    @PostMapping(value = "v1/student/class/add")
    public RespVo<String> studentClassAdd(StudentClassAddReqVo reqVo) {

        return new RespVo<>("新增成功");
    }

    @Operation(summary = "选择课时币接口", operationId = "classCoinList")
    @PostMapping(value = "v1/student/class/coin/list")
    public RespVo<List<SelectValueVo>> classCoinList() {
        return new RespVo<>(List.of(
                new SelectValueVo("2", "Any teacher with 2.00 tokens"),
                new SelectValueVo("2.5", "Any teacher with 2.50 tokens"),
                new SelectValueVo("3", "Any teacher with 3.00 tokens"),
                new SelectValueVo("3.5", "Any teacher with 3.50 tokens"),
                new SelectValueVo("4", "Any teacher with 4.00 tokens")
        ));
    }

    @Operation(summary = "选择老师接口", operationId = "classTeacherList")
    @PostMapping(value = "v1/student/class/teacher/list")
    public RespVo<List<SelectValueVo>> classTeacherList(@RequestBody StudentClassCommonQueryVo queryVo) {
        return new RespVo<>(List.of(
                new SelectValueVo("0", "Wang Lei"),
                new SelectValueVo("1", "Li Lei"),
                new SelectValueVo("2", "Wang Mei"),
                new SelectValueVo("3", "Zhang San"),
                new SelectValueVo("4", "Han Mei Mei")
        ));
    }

    @Operation(summary = "统计接口", operationId = "classTotalList")
    @PostMapping(value = "v1/student/class/total")
    public RespVo<StudentClassTotalRespVo> classTotalList(StudentClassQueryVo queryVo) {
        return new RespVo<StudentClassTotalRespVo>(new StudentClassTotalRespVo(new BigDecimal("12"), new BigDecimal("3")));
    }


}
