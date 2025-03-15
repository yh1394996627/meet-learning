package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseTypeEnum {

    SINGLE("单人", 1),
    GROUP("团体", 2),
    EXPERIENCE_TEST("体验测试", 3);


    private final String remark;

    private final Integer type;


    public static CourseTypeEnum getByType(Integer type) {
        for (CourseTypeEnum courseTypeEnum : values()) {
            if (courseTypeEnum.getType().equals(type)) {
                return courseTypeEnum;
            }
        }
        return null;
    }

}
