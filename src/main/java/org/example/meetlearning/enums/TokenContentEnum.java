package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenContentEnum {

    COURSE_CLASS("课程预约","Course reservation"),

    ;

    private final String content;
    private final String enContent;


}
