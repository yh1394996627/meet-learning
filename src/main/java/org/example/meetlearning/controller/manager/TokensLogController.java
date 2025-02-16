package org.example.meetlearning.controller.manager;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.token.TokensLogListRespVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Tag(name = "课时币接口")
@RestController
public class TokensLogController {

    @Operation(summary = "课时币记录", operationId = "tokensLogPage")
    @PostMapping(value = "v1/token/page")
    public RespVo<PageVo<TokensLogListRespVo>> tokensLogPage() {
        TokensLogListRespVo respVo = new TokensLogListRespVo();
        respVo.setRecordId("1122334455");
        respVo.setQuantity(new BigDecimal("12"));
        respVo.setRemark("232323");
        respVo.setBalance(new BigDecimal("12"));
        respVo.setCreateTime(new Date());
        respVo.setCreator("22222333");
        respVo.setCreateName("22223333");
        return new RespVo<>(PageVo.getNewPageVo(1, 10, 1, 1, List.of(respVo)));
    }

}
