package org.example.meetlearning.vo.common;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class PageRequestQuery<T> {

    @Schema(name = "current", description = "起始页数")
    private Integer current = 0;

    @Schema(name = "pageSize", description = "起始页数")
    private Integer pageSize = 20;

    public Page<T> getPageRequest() {
        return Page.of(current, pageSize);
    }

}
