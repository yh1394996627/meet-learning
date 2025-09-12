package org.example.meetlearning.service.impl;

import lombok.RequiredArgsConstructor;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.GroupClassStudentRecConverter;
import org.example.meetlearning.dao.entity.GroupClassStudentRec;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.mapper.GroupClassStudentRecMapper;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@RequiredArgsConstructor
public class GroupClassStudentRecService {

    private final GroupClassStudentRecMapper groupClassStudentRecMapper;

    /**
     * 新增 团体课程关系
     */
    public void addGroupClassStudent(String userCode, String userName, StudentClass studentClass) {
        GroupClassStudentRec studentRec = GroupClassStudentRecConverter.INSTANCE.toCreate(userCode, studentClass.getRecordId(), userCode, userName);
        insert(studentRec);
    }


    /**
     * 删除
     *
     * @param classId
     * @return
     */
    public int deleteByClassId(String classId) {
        return groupClassStudentRecMapper.deleteByClassId(classId);
    }

    /**
     * 删除
     *
     * @param classId
     * @param studentId
     * @return
     */
    public int deleteByClassIdStuId(String classId, String studentId) {
        return groupClassStudentRecMapper.deleteByClassIdStuId(classId, studentId);
    }

    /**
     * 新增
     *
     * @param record
     * @return
     */
    public int insert(GroupClassStudentRec record) {
        return groupClassStudentRecMapper.insert(record);
    }

    /**
     * 查询
     *
     * @param classId
     * @param studentId
     * @return
     */
    public GroupClassStudentRec selectByClassId(String classId, String studentId) {
        return groupClassStudentRecMapper.selectByClassId(classId, studentId);
    }

    /**
     * 修改
     *
     * @param record
     * @return
     */
    public int update(GroupClassStudentRec record) {
        return groupClassStudentRecMapper.update(record);
    }
}
