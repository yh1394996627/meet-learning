package org.example.meetlearning.vo.token;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.TokensLog;
import org.example.meetlearning.vo.common.PageRequestQuery;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class TokensLogQueryVo extends PageRequestQuery<TokensLog> {

    @Schema(name = "recordId", description = "用户ID")
    private String recordId;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.hasText(recordId)) {
            params.put("userId", recordId);
        }
        return params;
    }
}