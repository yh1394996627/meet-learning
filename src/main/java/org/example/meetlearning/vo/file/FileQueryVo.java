package org.example.meetlearning.vo.file;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FileQueryVo {

    @Schema(name = "current", description = "起始页数")
    private Integer current = 0;

    @Schema(name = "pageSize", description = "起始页数")
    private Integer pageSize = 20;

    @Schema(name = "path", description = "路径")
    private String path;

    @Schema(name = "isPrivate", description = "是否是私有文件夹")
    private Boolean isPrivate;

    @Schema(name = "userId", description = "用户ID 私有文件夹用")
    private String userId;

}
