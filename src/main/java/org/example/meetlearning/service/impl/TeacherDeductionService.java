package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TeacherDeductionConverter;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherDeduction;
import org.example.meetlearning.dao.mapper.StudentClassMapper;
import org.example.meetlearning.dao.mapper.TeacherDeductionMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@AllArgsConstructor
public class TeacherDeductionService {

    private final TeacherDeductionMapper teacherDeductionMapper;

    private final StudentClassMapper studentClassMapper;

    /**
     * 插入老师扣款信息
     */
    public void insertEntity(String userCode, String userName, String classId, BigDecimal deductionQty, String remark) {
        StudentClass studentClass = studentClassMapper.selectByRecordId(classId);
        TeacherDeduction teacherDeduction = TeacherDeductionConverter.INSTANCE.toCreate(userCode, studentClass, deductionQty, remark);
        teacherDeductionMapper.insertEntity(teacherDeduction);
    }

    /**
     * 查询老师当月扣款金额
     */
    public BigDecimal getMonthDeduction(String month, String teacherId) {
        return teacherDeductionMapper.selectMonthDeductionByTeacher(teacherId, month);
    }


    /**
     * 删除抵扣
     */
    public void deleteDeductionByClassId(String classId) {
        teacherDeductionMapper.deleteByClassId(classId);
    }


}
