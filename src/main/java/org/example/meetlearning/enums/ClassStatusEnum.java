package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ClassStatusEnum {

    NOT_STARTED("not started", "未开始", 0),
    PROCESS("process", "进行中", 1),
    FINISH("finish", "已完成", 2),;

    private final String entRemark;
    private final String remark;
    private final Integer status;

    public static ClassStatusEnum getCourseStatusByType(Integer status) {
        for (ClassStatusEnum courseStatus : ClassStatusEnum.values()) {
            if (status.equals(courseStatus.status)) {
                return courseStatus;
            }
        }
        return null;
    }

}
