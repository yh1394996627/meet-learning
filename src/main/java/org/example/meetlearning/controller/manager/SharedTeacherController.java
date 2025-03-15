package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.service.TeacherPcService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherListRespVo;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherPriceReqVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

@Tag(name = "共享老师接口")
@RestController
@AllArgsConstructor
public class SharedTeacherController {

    private final TeacherPcService teacherPcService;

    @Operation(summary = "共享老师列表", operationId = "sharedTeacherList")
    @PostMapping(value = "v1/shared/teacher/list")
    public RespVo<List<SharedTeacherListRespVo>> sharedTeacherList() {
        return teacherPcService.sharedTeacherList();
    }


    @Operation(summary = "共享老师设置价格", operationId = "sharedTeacherPriceSet")
    @PostMapping(value = "v1/shared/teacher/price/set")
    public RespVo<String> sharedTeacherPriceSet(@RequestBody SharedTeacherPriceReqVo reqVo) {
        return teacherPcService.sharedTeacherPriceSet(reqVo);
    }

}
