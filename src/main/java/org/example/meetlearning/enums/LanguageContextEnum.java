package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum LanguageContextEnum {
    OPERATION_SUCCESSFUL("Operation successful", "操作成功"),
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
