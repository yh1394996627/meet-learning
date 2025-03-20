package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum GenderEnum {

    MALE("男", "Male", 0),
    FEMALE("女", "Female", 1);

    private final String name;
    private final String enName;
    private final int type;


    public static GenderEnum getByEnName(String enName) {
        for (GenderEnum genderEnum : values()) {
            if (StringUtils.equals(genderEnum.getEnName(), enName)) {
                return genderEnum;
            }
        }
        return null;
    }


}
