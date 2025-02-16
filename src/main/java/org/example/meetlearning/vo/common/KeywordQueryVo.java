package org.example.meetlearning.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class KeywordQueryVo {

    @Schema(name = "keyword", description = "模糊匹配")
    private String keyword;

}
