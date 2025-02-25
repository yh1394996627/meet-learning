package org.example.meetlearning.enums;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RoleEnum {

    MANAGER("管理员", List.of("studentList", "studentClasses", "affiliateList", "teachersList", "sharedTeachers", "materials", "tokensLog")),
    AFFILIATE("代理商", new ArrayList<>()),
    TEACHER("老师", new ArrayList<>()),
    STUDENT("学生", new ArrayList<>());


    RoleEnum(String content, List<String> menus) {
        this.content = content;
        this.menus = menus;
    }

    private final String content;

    private final List<String> menus;


}
