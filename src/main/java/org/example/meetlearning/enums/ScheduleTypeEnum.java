package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ScheduleTypeEnum {
    REGULAR("常规"),
    SPECIAL("特殊"),
    OFF("排除");

    private final String content;

}
