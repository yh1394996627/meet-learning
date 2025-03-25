package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

@Getter
@AllArgsConstructor
public enum ScheduleWeekEnum {
    MONDAY("星期一",1),
    TUESDAY("星期二",2),
    WEDNESDAY("星期三",3),
    THURSDAY("星期四",4),
    FRIDAY("星期五",5),
    SATURDAY("星期六",6),
    SUNDAY("星期日",7);

    private final String content;

    private final Integer num;

    /**
     * 根据日期获取枚举
     */
    public static ScheduleWeekEnum getByDate(String date) {
        Integer week = LocalDate.parse(date).getDayOfWeek().getValue();
        for (ScheduleWeekEnum value : values()) {
            if (value.getNum().equals(week)) {
                return value;
            }
        }
        return null;
    }
}
