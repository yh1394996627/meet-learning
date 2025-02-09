package org.example.meetlearning.vo.affiliate;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class AffiliateRemarkUpdateReqVo {

    @Schema(name = "recordId", description = "业务Id")
    private String recordId;

    @Schema(name = "remark", description = "备注")
    private String remark;

}
