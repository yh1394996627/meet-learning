package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public enum CourseStatusEnum {

    NOT_STARTED("not started", "未开始", 1),
    PROCESS("process", "进行中", 2),
    FINISH("finish", "已完成", 3),
    BE_LATE("be late", "迟到", 4),
    LEAVE_EARLY("leave early", "早退", 5),
    ABSENT("absent", "缺席", 6),
    CANCEL("cancel", "取消课程", 7),
    CANCEL_DE("cancel deduction", "三天内取消课程-扣款一半薪资", 8),
    WAIT_ONT_STAR("one_star", "一星评价待处理", 10),
    ONT_STAR("one_star", "一星评价", 11),
    ;

    private final String entRemark;
    private final String remark;
    private final Integer status;

    public static CourseStatusEnum getCourseStatusByType(Integer status) {
        for (CourseStatusEnum courseStatus : CourseStatusEnum.values()) {
            if (courseStatus.status.equals(status)) {
                return courseStatus;
            }
        }
        return null;
    }

    public static Boolean isVerCourseStatus(Integer status) {
        CourseStatusEnum statusEnum = null;
        for (CourseStatusEnum courseStatus : CourseStatusEnum.values()) {
            if (courseStatus.status.equals(status)) {
                statusEnum = courseStatus;
            }
        }
        if (statusEnum == null) {
            return false;
        } else {
            List<CourseStatusEnum> verStatus = List.of(
                    CourseStatusEnum.ONT_STAR,
                    CourseStatusEnum.FINISH,
                    CourseStatusEnum.ABSENT,
                    CourseStatusEnum.CANCEL_DE
            );
            return verStatus.contains(statusEnum);
        }
    }
}
