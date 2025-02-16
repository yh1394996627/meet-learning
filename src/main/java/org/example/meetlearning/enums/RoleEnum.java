package org.example.meetlearning.enums;


import lombok.Getter;

@Getter
public enum RoleEnum {

    MANAGER("管理员"),
    AFFILIATE("代理商"),
    TEACHER("老师"),
    STUDENT("学生");


    RoleEnum(String content) {
        this.content = content;
    }

    private final String content;


}
