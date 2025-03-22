package org.example.meetlearning.vo.user;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.example.meetlearning.dao.entity.UserFinanceRecord;
import org.example.meetlearning.vo.common.PageRequestQuery;

import java.util.HashMap;
import java.util.Map;

@Data
public class UserStudentFinanceRecordQueryVo extends PageRequestQuery<UserFinanceRecord> {

    @Schema(name = "userId", description = "用户ID")
    private String userId;

    @Schema(name = "isComplete", description = "是否完成")
    private Boolean isComplete;

    @Schema(hidden = true)
    public Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        if (isComplete != null) {
            params.put("isComplete", isComplete);
        }
        return params;
    }

}
