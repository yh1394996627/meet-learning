package org.example.meetlearning.vo.textbook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.vo.common.PageRequestQuery;

import java.util.HashMap;
import java.util.Map;

@Data
public class TextbookQueryVo extends PageRequestQuery<Textbook> {

    @Schema(name = "keyword", description = "模糊匹配")
    private String keyword;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        if (StringUtils.isNotEmpty(keyword)) {
            params.put("keyword", keyword);
        }
        return params;
    }


}
