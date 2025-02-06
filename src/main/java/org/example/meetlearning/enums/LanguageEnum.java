package org.example.meetlearning.enums;

import lombok.Getter;

@Getter
public enum LanguageEnum {

    ENGLISH("English"),
    S_CHINESE("简体中文"),
    T_CHINESE("繁體中文"),
    JAPANESE("日本語"),
    KOREAN("한국어"),
    VIETNAMESE("Tiếng Việt"),
    ;

    private final String content;

    LanguageEnum(String content) {
        this.content = content;
    }
}
