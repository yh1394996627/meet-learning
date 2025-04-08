package org.example.meetlearning.converter;

import java.util.Date;

import lombok.Data;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherDeduction;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;

@Mapper
public interface TeacherDeductionConverter {

    TeacherDeductionConverter INSTANCE = Mappers.getMapper(TeacherDeductionConverter.class);

    default TeacherDeduction toCreate(String userCode, StudentClass studentClass, BigDecimal deductionQty, String remark) {
        TeacherDeduction teacherDeduction = new TeacherDeduction();
        teacherDeduction.setDeleted(false);
        teacherDeduction.setTeacherId(studentClass.getTeacherId());
        teacherDeduction.setClassId(studentClass.getRecordId());
        teacherDeduction.setDeductionQty(deductionQty);
        teacherDeduction.setDeductionDate(new Date());
        teacherDeduction.setCreator(userCode);
        teacherDeduction.setCreateTime(new Date());
        teacherDeduction.setRemark(remark);
        return teacherDeduction;
    }

}
