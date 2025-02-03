package org.example.meetlearning.vo.student;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentRecordReqVo {

    @Schema(name = "recordId", description = "业务Id")
    private String recordId;

}
