package org.example.meetlearning.vo.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RecordIdPageQueryVo<T> extends PageRequestQuery<T> {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;


}
