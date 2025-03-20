package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ConfigTypeEnum {
    COUNTRY("国家", "country"),
    LANGUAGE("语言", "language"),
    CURRENCY("货币", "currency");

    private final String content;
    private final String enContent;
}
