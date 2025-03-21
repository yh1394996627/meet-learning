package org.example.meetlearning.enums;


import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum RoleEnum {

    MANAGER("管理员", List.of("studentList", "studentClasses", "affiliateList", "teachersList", "sharedTeachers", "materials", "tokensLog", "config")),
    AFFILIATE("代理商", List.of("studentList", "studentClasses", "materials", "tokensLog")),
    TEACHER("老师", List.of("bookedClasses", "schedule", "materials", "teachersList")),
    STUDENT("学生", List.of("myBook", "bookCourse", "materials", "tokensLog"));


    RoleEnum(String content, List<String> menus) {
        this.content = content;
        this.menus = menus;
    }

    private final String content;

    private final List<String> menus;


}
