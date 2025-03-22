package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.TokenConverter;
import org.example.meetlearning.dao.entity.BaseConfig;
import org.example.meetlearning.dao.entity.TokensLog;
import org.example.meetlearning.service.impl.BaseConfigService;
import org.example.meetlearning.service.impl.TokensLogService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.token.TokensLogAddReqVo;
import org.example.meetlearning.vo.token.TokensLogListRespVo;
import org.example.meetlearning.vo.token.TokensLogQueryVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class TokensLogPcService {

    private final TokensLogService tokensLogService;

    private final BaseConfigService baseConfigService;

    public RespVo<PageVo<TokensLogListRespVo>> tokensLogPage(String userCode, String userName, TokensLogQueryVo queryVo) {
        Page<TokensLog> page = tokensLogService.selectPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        PageVo<TokensLogListRespVo> pageVO = PageVo.map(page, list -> TokenConverter.INSTANCE.toListVo(userCode, userName, list));
        return new RespVo<>(pageVO);
    }


    public RespVo<String> addTokensLog(String userCode, String userName, TokensLogAddReqVo tokensLogAddReqVo) {
        try {
            Assert.isTrue(StringUtils.isNotEmpty(tokensLogAddReqVo.getUserId()), "user is not null");
            TokensLog tokensLog = TokenConverter.INSTANCE.toCreateToken(userCode, userName, tokensLogAddReqVo);
            if(StringUtils.isNotEmpty(tokensLogAddReqVo.getCurrencyCode())){
                BaseConfig baseConfig = baseConfigService.selectByCode(tokensLogAddReqVo.getCurrencyCode());
                Assert.notNull(baseConfig, "Configuration information not obtained record:【" + tokensLogAddReqVo.getCurrencyCode() + "】");
                tokensLog.setCurrencyCode(baseConfig.getCode());
                tokensLog.setCurrencyName(baseConfig.getName());
            }
            tokensLogService.insertEntity(tokensLog);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("New failed", ex);
            return new RespVo<>(null, false, "New failed, unknown error!");
        }
    }

}
