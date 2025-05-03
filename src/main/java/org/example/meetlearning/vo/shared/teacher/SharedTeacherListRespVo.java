package org.example.meetlearning.vo.shared.teacher;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SharedTeacherListRespVo {

    @Schema(name = "recordId", description = "业务ID")
    private String recordId;

    @Schema(name = "avatarUtl", description = "头像地址")
    private String avatarUtl;

    @Schema(name = "name", description = "名称")
    private String name;

    @Schema(name = "country", description = "国家")
    private String country;

    @Schema(name = "videoUrl", description = "视频地址")
    private String videoUrl;

    @Schema(name = "creditsPrice", description = "积分单价")
    private BigDecimal creditsPrice;

    @Schema(name = "coin", description = "课时币单价")
    private BigDecimal coin;

    @Schema(name = "groupCoin", description = "课时币团体课单价")
    private BigDecimal groupCoin;

}
