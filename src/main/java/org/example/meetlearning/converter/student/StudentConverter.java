package org.example.meetlearning.converter.student;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.student.StudentAddReqVo;
import org.example.meetlearning.vo.student.StudentListRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentConverter {

    StudentConverter INSTANCE = Mappers.getMapper(StudentConverter.class);

    StudentListRespVo toStudentListRespVo(Student student);

    default Student toCreateStudent(String userCode, String userName, StudentAddReqVo reqVo) {
        Student student = new Student();
        student.setDeleted(false);
        student.setCreator(userCode);
        student.setCreateName(userName);
        student.setCreateTime(new Date());
        student.setRecordId(UUID.randomUUID().toString());
        student.setEnName(reqVo.getEnName());
        student.setEncryption(MD5Util.getEncryption());
        student.setPassword(MD5Util.md5(student.getEncryption(), reqVo.getPassword()));
        student.setWebsite(reqVo.getWebsite());
        student.setLanguage(reqVo.getLanguage());
        return student;
    }


}
