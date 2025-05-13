package org.example.meetlearning.controller.manager;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.controller.BaseController;
import org.example.meetlearning.converter.TextbookConverter;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.service.TextbookPcService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.textbook.TextbookQueryVo;
import org.example.meetlearning.vo.textbook.TextbookRecordReqVo;
import org.example.meetlearning.vo.textbook.TextbookReqVo;
import org.example.meetlearning.vo.textbook.TextbookRespVo;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "教材管理")
@RestController
@Slf4j
@AllArgsConstructor
public class TextbookController implements BaseController {

    private final TextbookPcService pcService;

    @Operation(summary = "教材查询", operationId = "textbookPage")
    @PostMapping(value = "v1/textbook/list")
    public RespVo<PageVo<TextbookRespVo>> textbookPage(@RequestBody TextbookQueryVo queryVo) {
        PageVo<TextbookRespVo> page = pcService.textbookPage(queryVo);
        return new RespVo<>(page);
    }

    @Operation(summary = "教材下拉查询", operationId = "textbookSelect")
    @PostMapping(value = "v1/textbook/select")
    public RespVo<List<SelectValueVo>> textbookSelect(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(pcService.selectValueVos(queryVo));
    }

    @Operation(summary = "教材新增", operationId = "add")
    @PostMapping(value = "v1/textbook/add")
    public RespVo<String> add(@RequestBody TextbookReqVo reqVo) {
        pcService.add(getUserCode(), getUserName(), reqVo);
        return new RespVo<>("Operation successful");
    }

    @Operation(summary = "教材更新", operationId = "update")
    @PostMapping(value = "v1/textbook/update")
    public RespVo<String> update(@RequestBody TextbookReqVo reqVo) {
        pcService.update(getUserCode(), getUserName(), reqVo);
        return new RespVo<>("Operation successful");
    }

    @Operation(summary = "教材删除", operationId = "deleted")
    @PostMapping(value = "v1/textbook/delete")
    public RespVo<String> deleted(@RequestBody RecordIdQueryVo queryVo) {
        pcService.deleted(queryVo);
        return new RespVo<>("Operation successful");
    }

    @Operation(summary = "根据教材ID查询绑定材料", operationId = "fileSearch")
    @PostMapping(value = "v1/textbook/file")
    public RespVo<List<TextbookRecordReqVo>> fileSearch(@RequestBody RecordIdQueryVo queryVo) {
        return new RespVo<>(pcService.fileSearch(queryVo));
    }
}
