package org.example.meetlearning.vo.student;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class StudentRemarkUpdateReqVo {

    @Schema(name = "recordId", description = "业务Id")
    private String recordId;

    @Schema(name = "remark", description = "备注")
    private String remark;

}
