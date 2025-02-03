package org.example.meetlearning.enums;

import lombok.Getter;

@Getter
public enum CourseTypeEnum {

    SINGLE("单人"),
    GROUP("团体"),
    EXPERIENCE_TEST("体验测试")
    ;


    CourseTypeEnum(String remark) {
        this.remark = remark;
    }

    private final String remark;

}
