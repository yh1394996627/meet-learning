package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.StudentClassAddReqVo;
import org.example.meetlearning.vo.classes.StudentClassListRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Objects;
import java.util.UUID;

@Mapper
public interface StudentClassConverter {

    StudentClassConverter INSTANCE = Mappers.getMapper(StudentClassConverter.class);

    default StudentClassListRespVo toStudentClassListRespVo(StudentClass studentClass) {
        StudentClassListRespVo respVo = new StudentClassListRespVo();
        respVo.setRecordId(studentClass.getRecordId());
        respVo.setStudentId(studentClass.getStudentId());
        respVo.setStudentName(studentClass.getStudentName());
        respVo.setStudentLanguage(studentClass.getStudentCountry());
        respVo.setTeacherId(studentClass.getTeacherId());
        respVo.setTeacherName(studentClass.getTeacherName());
        respVo.setTeacherLanguage(studentClass.getTeacherCountry());
        respVo.setCourseName(studentClass.getCourseName());
        respVo.setCourseTime(studentClass.getCourseTime().toString() + " " + studentClass.getBeginTime() + "-" + studentClass.getEndTime());
        respVo.setCourseType(studentClass.getCourseType());
        if (studentClass.getStudentCourseStatus() != null) {
            respVo.setStudentCourseStatusContent(Objects.requireNonNull(CourseStatusEnum.getCourseStatusByType(studentClass.getStudentCourseStatus())).name());
        }
        if (studentClass.getTeacherCourseStatus() != null) {
            respVo.setTeacherCourseStatusContent(Objects.requireNonNull(CourseStatusEnum.getCourseStatusByType(studentClass.getTeacherCourseStatus())).name());
        }
        respVo.setCourseVideoUrl(studentClass.getCourseVideoUrl());
        respVo.setIsCourseVideoExpired(studentClass.getIsCourseVideoExpired());
        respVo.setAffiliateId(studentClass.getAffiliateId());
        respVo.setAffiliateName(studentClass.getAffiliateName());
        respVo.setStudentConsumption(studentClass.getStudentConsumption());
        respVo.setStudentBalance(studentClass.getStudentBalance());
        respVo.setEfficientDate(studentClass.getEfficientDate());
        return respVo;
    }

    default StudentClass toCreate(String userCode, String userName, StudentClassAddReqVo reqVo, Student student, Teacher teacher, Affiliate affiliate) {
        StudentClass studentClass = new StudentClass();
        studentClass.setDeleted(false);
        studentClass.setCreator(userCode);
        studentClass.setCreateName(userName);
        studentClass.setCreateTime(new Date());
        studentClass.setRecordId(UUID.randomUUID().toString());
        studentClass.setTeacherCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setStudentCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setCourseVideoUrl(null);
        studentClass.setZoomId(null);
        studentClass.setIsCourseVideoExpired(false);
        studentClass.setStudentId(student.getId());
        studentClass.setStudentName(student.getName());
        studentClass.setStudentCountry(student.getCountry());
        studentClass.setTeacherId(teacher.getId());
        studentClass.setTeacherName(teacher.getName());
        studentClass.setTeacherCountry(teacher.getCountry());
        studentClass.setCourseName(reqVo.getCourseId());
        studentClass.setCourseName(reqVo.getCourseId());
        if (reqVo.getCourseType() != null) {
            CourseTypeEnum courseTypeEnum = CourseTypeEnum.getByType(reqVo.getCourseType());
            assert courseTypeEnum != null;
            studentClass.setCourseType(courseTypeEnum.name());
        }
        studentClass.setCourseTime(reqVo.getCourseDate());
        if(StringUtils.isNotEmpty(reqVo.getCourseTime())){
            String [] arr = StringUtils.split(reqVo.getCourseTime(), "-");
            studentClass.setBeginTime(arr[0]);
            studentClass.setEndTime(arr[1]);
        }
        if (affiliate != null) {
            studentClass.setAffiliateId(affiliate.getRecordId());
            studentClass.setAffiliateName(affiliate.getName());
        }
        //todo 这块还需要实现
//        studentClass.setStudentConsumption(BigDecimalUtil.nullOrZero(student.getConsumption()).add(BigDecimalUtil.nullOrZero(reqVo.getQuantity())));
//        studentClass.setEfficientDate(new Date());
//        studentClass.setStudentBalance(BigDecimalUtil.nullOrZero(student.getBalance()));
        return studentClass;
    }


}
