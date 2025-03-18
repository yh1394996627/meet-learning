package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TeacherSpecialistsEnum {
    ADULTS("成人"),
    TEENS("青少年"),
    KIDS("儿童"),
    ;

    private final String content;
}
