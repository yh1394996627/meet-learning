package org.example.meetlearning.converter;

import java.util.Date;
import java.util.UUID;

import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.student.StudentAddReqVo;
import org.example.meetlearning.vo.student.StudentInfoRespVo;
import org.example.meetlearning.vo.student.StudentListRespVo;
import org.example.meetlearning.vo.student.StudentUpdateReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StudentConverter {

    StudentConverter INSTANCE = Mappers.getMapper(StudentConverter.class);

    default StudentListRespVo toStudentListRespVo(Student student) {
        StudentListRespVo respVo = new StudentListRespVo();
        if (student == null) {
            return null;
        }
        respVo.setName(student.getName());
        respVo.setRecordId(student.getRecordId());
        respVo.setPhone(student.getPhone());
        respVo.setEmail(student.getEmail());
        respVo.setBalance(BigDecimalUtil.nullOrZero(student.getBalance()));
        respVo.setExpirationTime(student.getExpirationTime());
        respVo.setLastActivities(student.getLastActivities());
        respVo.setWebsite(student.getWebsite());
        respVo.setRemark(student.getRemark());
        respVo.setIsDeleted(BigDecimalUtil.eqZero(student.getBalance()));
        return respVo;
    }

    default Student toCreateStudent(String userCode, String userName, StudentAddReqVo reqVo) {
        Student student = new Student();
        student.setDeleted(false);
        student.setCreator(userCode);
        student.setCreateName(userName);
        student.setCreateTime(new Date());
        student.setRecordId(UUID.randomUUID().toString());
        student.setName(reqVo.getEnName());
        student.setEncryption("MD5");
        student.setPassword(MD5Util.md5(student.getEncryption(), reqVo.getPassword()));
        student.setWebsite(reqVo.getWebsite());
        student.setLanguage(reqVo.getLanguage().name());
        return student;
    }

    default Student toUpdateStudent(Student student, StudentUpdateReqVo reqVo) {
        student.setName(reqVo.getName());
        student.setAge(reqVo.getAge());
        student.setGender(reqVo.getGender());
        student.setLearnPurpose(reqVo.getLearnPurpose());
        student.setLearnPlan(reqVo.getLearnPlan());
        student.setRecommender(reqVo.getRecommender());
        return student;
    }


    default StudentInfoRespVo toStudentInfoRespVo(Student student) {
        StudentInfoRespVo studentInfoRespVo = new StudentInfoRespVo();
        studentInfoRespVo.setRecordId(student.getRecordId());
        studentInfoRespVo.setName(student.getName());
        studentInfoRespVo.setAge(student.getAge());
        studentInfoRespVo.setGender(student.getGender());
        studentInfoRespVo.setPhone(student.getPhone());
        studentInfoRespVo.setEmail(student.getEmail());
        studentInfoRespVo.setBalance(student.getBalance());
        studentInfoRespVo.setCountry(student.getCountry());
        studentInfoRespVo.setExpirationTime(student.getExpirationTime());
        studentInfoRespVo.setLastActivities(student.getLastActivities());
        studentInfoRespVo.setWebsite(student.getWebsite());
        studentInfoRespVo.setRemark(student.getRemark());
        studentInfoRespVo.setLearnPurpose(student.getLearnPurpose());
        studentInfoRespVo.setLearnPlan(student.getLearnPlan());
        studentInfoRespVo.setLanguage(student.getLanguage());
        studentInfoRespVo.setAffiliateId(student.getAffiliateId());
        studentInfoRespVo.setAffiliateName(student.getAffiliateName());
        return studentInfoRespVo;
    }
}
