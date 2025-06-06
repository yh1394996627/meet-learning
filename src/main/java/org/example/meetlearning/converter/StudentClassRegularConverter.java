package org.example.meetlearning.converter;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.StudentClassRegularReqVo;
import org.example.meetlearning.vo.classes.StudentClassRegularRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Mapper
public interface StudentClassRegularConverter {

    StudentClassRegularConverter INSTANCE = Mappers.getMapper(StudentClassRegularConverter.class);

    default StudentClassRegular toCreate(String userCode, String userName, StudentClassRegularReqVo reqVo, Student student, Teacher teacher) {
        StudentClassRegular regular = new StudentClassRegular();
        regular.setRecordId(UUID.randomUUID().toString());
        regular.setDeleted(false);
        regular.setCreator(userCode);
        regular.setCreateName(userName);
        regular.setCreateTime(new Date());
        regular.setStudentId(reqVo.getStudentId());
        regular.setStudentName(student.getName());
        regular.setStudentEmail(student.getEmail());
        regular.setTeacherId(teacher.getRecordId());
        regular.setTeacherName(student.getName());
        regular.setCourseId(reqVo.getCourseId());
        regular.setCourseName(reqVo.getCourseName());
        regular.setCourseTime(new Date());
        regular.setCourseType(reqVo.getCourseType());
        regular.setAuditStatus(-1);
        regular.setPrice(BigDecimalUtil.nullOrZero(teacher.getCoin()).multiply(new BigDecimal(reqVo.getCourseDates().size())).multiply(new BigDecimal(reqVo.getCourseTimes().size())));
        regular.setCreditsPrice(new BigDecimal("0"));
        return regular;
    }


    default StudentClassRegularRecord toCreateRecord(StudentClassRegular studentClassRegular, String courseDate) {
        StudentClassRegularRecord regularRecord = new StudentClassRegularRecord();
        regularRecord.setCreator(studentClassRegular.getCreator());
        regularRecord.setCreateName(studentClassRegular.getCreateName());
        regularRecord.setCreateTime(new Date());
        regularRecord.setCourseTime(DateUtil.parse(courseDate, "yyyy-MM-dd"));
        regularRecord.setBeginTime(studentClassRegular.getBeginTime());
        regularRecord.setEndTime(studentClassRegular.getEndTime());
        regularRecord.setRegularId(studentClassRegular.getRecordId());
        return regularRecord;
    }

    default StudentClassRegularRespVo toRespVo(StudentClassRegular studentClassRegular, List<Date> courseDates, List<String> courseTimes) {
        StudentClassRegularRespVo respVo = new StudentClassRegularRespVo();
        respVo.setRecordId(studentClassRegular.getRecordId());
        respVo.setStudentId(studentClassRegular.getStudentId());
        respVo.setStudentName(studentClassRegular.getStudentName());
        respVo.setStudentEmail(studentClassRegular.getStudentEmail());
        respVo.setCourseType(studentClassRegular.getCourseType());
        respVo.setCourseName(studentClassRegular.getCourseName());
        respVo.setCourseTime(StringUtils.join(courseTimes, ";"));
        String dateStr = StringUtils.join(courseDates.stream().map(date -> DateUtil.format(date, "yyyy-MM-dd")).toList(), ";");
        respVo.setCourseDateList(dateStr);
        return respVo;
    }

    default StudentClass toStudentClass(StudentClassRegular studentClassRegular, StudentClassRegularRecord studentClassRegularRecord) {
        StudentClass studentClasses = new StudentClass();
        studentClasses.setTeacherId(studentClassRegular.getTeacherId());
        studentClasses.setStudentId(studentClassRegular.getStudentId());
        studentClasses.setTeacherName(studentClassRegular.getTeacherName());
        studentClasses.setCourseTime(studentClassRegularRecord.getCourseTime());
        studentClasses.setBeginTime(studentClassRegularRecord.getBeginTime());
        studentClasses.setEndTime(studentClassRegularRecord.getEndTime());
        studentClasses.setCreator(studentClassRegular.getCreator());
        studentClasses.setCreateName(studentClassRegular.getCreateName());
        studentClasses.setCreateTime(new Date());
        studentClasses.setCourseType(studentClassRegular.getCourseType());
        studentClasses.setRecordId(studentClassRegular.getRecordId());
        return studentClasses;
    }

}
