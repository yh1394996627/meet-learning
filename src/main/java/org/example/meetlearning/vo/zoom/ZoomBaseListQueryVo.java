package org.example.meetlearning.vo.zoom;


import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class ZoomBaseListQueryVo {

    @Schema(name = "type", description = "")
    private Integer type;

    @Schema(name = "emailKeyword", description = "邮箱模糊匹配")
    private String emailKeyword;

    @Schema(name = "zoomStatus", description = "1正常 0异常")
    private Integer zoomStatus;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        if (type != null) {
            params.put("zoomType", type);
        }
        if (StringUtils.isNotEmpty(emailKeyword)) {
            params.put("emailKeyword", "%" + emailKeyword + "%");
        }
        if (zoomStatus != null) {
            params.put("zoomStatus", zoomStatus);
        }
        return params;
    }
}
