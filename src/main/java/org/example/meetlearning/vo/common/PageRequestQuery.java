package org.example.meetlearning.vo.common;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.data.domain.Sort;

@Data
public class PageRequestQuery<T> {

    @Schema(name = "current", description = "起始页数")
    private Integer current = 0;

    @Schema(name = "pageSize", description = "起始页数")
    private Integer pageSize = 20;

    @Schema(name = "orderBy", description = "排序字段")
    private String orderBy;

    @Schema(name = "direction", description = "排序 ASC:正序 DESC:倒叙")
    private String direction = "ASC";


    @Schema(hidden = true)
    public Page<T> getPageRequest() {
        return Page.of(current, pageSize);
    }

}
