package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum LanguageContextEnum {
    OPERATION_SUCCESSFUL("Operation successful", "操作成功"),
    OBJECT_NOTNULL("No object information obtained，Please refresh and try again", "未获取到对象信息，请刷新后重试"),
    STUDENT_NOTNULL("Student information not obtained，Please refresh and try again", "未获取到学生信息，请刷新后重试"),
    TEACHER_NOTNULL("Teacher information not obtained，Please refresh and try again", "未获取到教师信息，请刷新后重试"),
    REGULAR_NOTNULL("Fixed class record information not obtained, please refresh and try again", "未获取到固定上课记录信息，请刷新后重新尝试"),
    TEACHER_TIME_REPEAT("there is already an appointment, unable to make an appointment", "已经有预约了，无法预约"),
    USER_NOTNULL("User information not obtained", "未获取到用户信息"),
    USER_FINANCE_NOTNULL("To obtain management financial information", "未获取到用户账单信息"),
    OBJ_NOTNULL(" cannot be empty", " 不能为空"),
    USER_EXISTS("The user already exists", " 用户已存在"),
    TEACHER_CAN_DELETED("The teacher is in the enabled state and cannot be deleted", "老师是启用状态无法删除"),
    INSUFFICIENT_BALANCE("Insufficient balance, unable to operate", "余额不足，无法操作"),
    MEETING_FIVE("You can only enter the meeting five minutes in advance", "您只能提前五分钟进入会议"),
    USER_ALREADY("The user already exists and cannot be added", "用户已存在无法添加"),
    USER_EXIST("The user already exists", "用户已存在"),
    NOT_CHANGE("The course has already started and cannot be changed", "课程已经开始，无法更改"),
    NOT_CHANGE_TIME("There are still 3 hours left until the start of the course and the time cannot be changed", "距离课程开始还有3个小时，时间不能更改"),

    ;

    private final String en;
    private final String zh;


    public String getMessage(String language) {
        if (StringUtils.equals("zh", language)) {
            return zh;
        }
        return en;
    }
}
