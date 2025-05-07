package org.example.meetlearning.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TokenContentEnum {

    COURSE_CLASS("课程预约","Course reservation"),
    WECHAT_RECHARGE("微信充值","WeChat recharge"),

    ;

    private final String content;
    private final String enContent;


}
