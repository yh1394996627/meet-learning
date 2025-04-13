package org.example.meetlearning.vo.remark;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class TeacherClassRemarkReqVo {

    @Schema(name = "recordId", description = "课程ID")
    private String recordId;

    @Schema(name = "filePage", description = "页数")
    private BigDecimal filePage;

    @Schema(name = "classRemark", description = "上课备注")
    private String classRemark;

    @Schema(name = "remark", description = "备注")
    private String remark;

}
