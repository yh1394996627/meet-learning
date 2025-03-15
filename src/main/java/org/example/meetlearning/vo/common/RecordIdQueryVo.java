package org.example.meetlearning.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecordIdQueryVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;


}
