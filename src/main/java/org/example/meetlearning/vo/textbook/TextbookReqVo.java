package org.example.meetlearning.vo.textbook;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class TextbookReqVo {

    @Schema(name = "recordId", description = "记录id 修改时填写")
    private String recordId;

    @Schema(name = "name", description = "教材名称")
    private String name;

    @Schema(name = "levelBegin", description = "教材级别开始")
    private Integer levelBegin;

    @Schema(name = "levelEnd", description = "教材级别结束")
    private Integer levelEnd;

    @Schema(name = "type", description = "教材类型")
    private Integer type;

    @Schema(name = "catalog", description = "教材目录")
    private String catalog;

}



