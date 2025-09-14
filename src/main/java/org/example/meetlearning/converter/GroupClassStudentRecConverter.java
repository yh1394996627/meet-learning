package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.GroupClassStudentRec;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;

@Mapper
public interface GroupClassStudentRecConverter {

    GroupClassStudentRecConverter INSTANCE = Mappers.getMapper(GroupClassStudentRecConverter.class);

    default GroupClassStudentRec toCreate(String userCode, String classId, String studentId, String studentName) {
        GroupClassStudentRec groupClassStudentRec = new GroupClassStudentRec();
        groupClassStudentRec.setCreator(userCode);
        groupClassStudentRec.setCreateTime(new Date());
        groupClassStudentRec.setClassId(classId);
        groupClassStudentRec.setStudentId(studentId);
        groupClassStudentRec.setStudentName(studentName);
        groupClassStudentRec.setStudentCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        groupClassStudentRec.setDeleted(false);
        return groupClassStudentRec;
    }


}
