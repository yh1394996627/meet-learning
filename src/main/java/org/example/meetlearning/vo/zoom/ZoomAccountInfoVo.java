package org.example.meetlearning.vo.zoom;


import lombok.Data;

@Data
public class ZoomAccountInfoVo {

    private String id;
    private String account_name;
    private String plan_type; // 1=免费, 2=Pro, 3=商业, 4=企业
    private String plan_version; // 具体版本名称
    private String status; // 账户状态
}
