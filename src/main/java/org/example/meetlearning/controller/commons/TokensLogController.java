package org.example.meetlearning.controller.commons;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.example.meetlearning.controller.BaseHandler;
import org.example.meetlearning.service.TokensLogPcService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.token.TokensLogAddReqVo;
import org.example.meetlearning.vo.token.TokensLogListRespVo;
import org.example.meetlearning.vo.token.TokensLogQueryVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "课时币接口")
@RestController
@AllArgsConstructor
public class TokensLogController implements BaseHandler {

    private final TokensLogPcService tokensLogPcService;


    @Operation(summary = "课时币记录", operationId = "tokensLogPage")
    @PostMapping(value = "v1/token/page")
    public RespVo<PageVo<TokensLogListRespVo>> tokensLogPage(@RequestBody TokensLogQueryVo queryVo) {
        return tokensLogPcService.tokensLogPage(getUserCode(), getUserName(), queryVo);
    }

    @Operation(summary = "课时币记录", operationId = "tokensLogPage")
    @PostMapping(value = "v1/token/record/add")
    public RespVo<String> addTokensLog(@RequestBody TokensLogAddReqVo tokensLogAddReqVo) {
        return tokensLogPcService.addTokensLog(getUserCode(), getUserName(), tokensLogAddReqVo);
    }




}
