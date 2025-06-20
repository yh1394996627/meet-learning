package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum LanguageContextEnum {
    OPERATION_SUCCESSFUL("Operation successful", "操作成功"),
    OBJECT_NOTNULL("No object information obtained，Please refresh and try again", "未获取到对象信息，请刷新后重试"),



    ;

    private final String zh;
    private final String en;

    public String getMessage(String language) {
        if (StringUtils.equals("zh", language)) {
            return zh;
        }
        return en;
    }
}
