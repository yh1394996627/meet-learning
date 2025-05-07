package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayStatusEnum {

    CREATED("创建", 0),
    SUCCESS("成功", 1),
    CLOSE("关闭", 2),

    ;
    private final String content;
    private final Integer status;


}

