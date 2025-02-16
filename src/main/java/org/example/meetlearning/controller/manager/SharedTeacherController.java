package org.example.meetlearning.controller.manager;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
public class SharedTeacherController {

    @Operation(summary = "共享老师列表", operationId = "sharedTeacherList")
    @PostMapping(value = "v1/shared/teacher/list")
    public RespVo<List<SharedTeacherListRespVo>> sharedTeacherList() {
        SharedTeacherListRespVo respVo = new SharedTeacherListRespVo();
        respVo.setRecordId("123123123123");
        respVo.setAvatarUtl("");
        respVo.setName("112233");
        respVo.setCountry("菲利宾");
        respVo.setVideoUrl("");
        respVo.setCreditsPrice(new BigDecimal("12"));
        respVo.setPrice(new BigDecimal("0"));
        return new RespVo<>(List.of(respVo));
    }


    @Operation(summary = "共享老师列表", operationId = "sharedTeacherPriceSet")
    @PostMapping(value = "v1/shared/teacher/price/set")
    public RespVo<String> sharedTeacherPriceSet(@RequestBody SharedTeacherPriceReqVo reqVo) {
        return new RespVo<>("设置完成");
    }

}
