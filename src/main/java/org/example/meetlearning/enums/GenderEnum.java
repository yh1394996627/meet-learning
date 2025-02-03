package org.example.meetlearning.enums;

import lombok.Getter;

@Getter
public enum GenderEnum {

    MALE("男",0),
    FEMALE("女",1)
    ;

    GenderEnum(String name, int type) {
        this.name = name;
        this.type = type;
    }

    private final String name;
    private final int type;


}
