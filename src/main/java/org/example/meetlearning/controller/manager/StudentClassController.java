package org.example.meetlearning.controller.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.vo.classes.StudentClassAddReqVo;
import org.example.meetlearning.vo.classes.StudentClassListRespVo;
import org.example.meetlearning.vo.classes.StudentClassQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "学生预约课程接口")
@RestController
@Slf4j
public class StudentClassController {


    @Operation(summary = "学生预约记录列表", operationId = "studentClassPage")
    @PostMapping(value = "v1/student/class/page")
    public RespVo<Page<StudentClassListRespVo>> studentClassPage(StudentClassQueryVo queryVo) {

        return new RespVo<>(null);
    }


    @Operation(summary = "新增学生预约", operationId = "studentClassAdd")
    @PostMapping(value = "v1/student/class/add")
    public RespVo<String> studentClassAdd(StudentClassAddReqVo reqVo) {

        return new RespVo<>(null);
    }

    @Operation(summary = "选择课时币接口", operationId = "classCoinList")
    @PostMapping(value = "v1/student/class/coin/list")
    public RespVo<List<SelectValueVo>> classCoinList(StudentClassAddReqVo reqVo) {
        return new RespVo<>(List.of(
                new SelectValueVo("2","Any teacher with 2.00 tokens"),
                new SelectValueVo("2.5","Any teacher with 2.50 tokens"),
                new SelectValueVo("3","Any teacher with 3.00 tokens"),
                new SelectValueVo("3.5","Any teacher with 3.50 tokens"),
                new SelectValueVo("4","Any teacher with 4.00 tokens")
                ));
    }

    @Operation(summary = "选择老师接口", operationId = "classTeacherList")
    @PostMapping(value = "v1/student/class/teacher/list")
    public RespVo<List<SelectValueVo>> classTeacherList(StudentClassAddReqVo reqVo) {
        return new RespVo<>(List.of(
                new SelectValueVo("0","Wang Lei"),
                new SelectValueVo("1","Li Lei"),
                new SelectValueVo("2","Wang Mei"),
                new SelectValueVo("3","Zhang San"),
                new SelectValueVo("4","Han Mei Mei")
        ));
    }





}
