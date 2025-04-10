package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CourseStatusEnum {

    NOT_STARTED("not started", "未开始", 1),
    PROCESS("process", "进行中", 2),
    FINISH("finish", "已完成", 3),
    BE_LATE("be late", "迟到", 4),
    LEAVE_EARLY("leave early", "早退", 5),
    ABSENT("absent", "缺席", 6);

    private final String entRemark;
    private final String remark;
    private final Integer status;

    public static CourseStatusEnum getCourseStatusByType(Integer status) {
        for (CourseStatusEnum courseStatus : CourseStatusEnum.values()) {
            if (status.equals(courseStatus.status)) {
                return courseStatus;
            }
        }
        return null;
    }

}
