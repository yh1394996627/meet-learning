package org.example.meetlearning.controller.manager;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.service.StudentPcService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "学生接口")
@RestController
@Slf4j
public class StudentController implements BaseController {

    @Autowired
    private StudentPcService studentPcService;

    @Operation(summary = "学生列表", operationId = "studentPage")
    @PostMapping(value = "v1/student/page")
    public RespVo<PageVo<StudentListRespVo>> studentPage(StudentRequestQueryVo queryVo) {
        return studentPcService.studentPage(getUserCode(), queryVo);
    }


    @Operation(summary = "新增学生接口", operationId = "studentAdd")
    @PostMapping(value = "v1/student/add")
    public RespVo<String> studentAdd(StudentAddReqVo reqVo) {

        return new RespVo<>("新增成功");
    }


    @Operation(summary = "更新学生接口", operationId = "studentUpdate")
    @PostMapping(value = "v1/student/update")
    public RespVo studentUpdate(StudentUpdateReqVo reqVo) {

        return new RespVo<>("更新成功");
    }


    @Operation(summary = "删除学生接口", operationId = "studentDeleted")
    @PostMapping(value = "v1/student/deleted")
    public RespVo studentDeleted(StudentRecordReqVo reqVo) {

        return new RespVo<>("删除成功");
    }


    @Operation(summary = "更新学生备注", operationId = "studentRemarkUpdate")
    @PostMapping(value = "v1/student/remark/update")
    public RespVo studentRemarkUpdate(StudentRemarkUpdateReqVo reqVo) {

        return new RespVo<>("更新成功");
    }


    @Operation(summary = "查询课时币记录", operationId = "studentRemarkUpdate")
    @PostMapping(value = "v1/student/statement/record/page")
    public RespVo<Page<StudentStatementRecordReqVo>> studentStatementRecordPage(StudentRecordReqVo reqVo) {

        return new RespVo<>(null);
    }


}
