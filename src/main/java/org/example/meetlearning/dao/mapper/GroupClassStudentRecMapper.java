package org.example.meetlearning.dao.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.GroupClassStudentRec;

public interface GroupClassStudentRecMapper {

    int deleteByClassIdStuId(String classId,String studentId);

    int deleteByClassId(String classId);

    int insert(GroupClassStudentRec record);

    List<GroupClassStudentRec> selectByStudentId(String studentId);

    int update(GroupClassStudentRec record);

}