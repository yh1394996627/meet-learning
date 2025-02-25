package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CountryEnum {

    China("中华人民共和国"),
    Philippines("菲利宾"),
    ;

    private final String content;


}
