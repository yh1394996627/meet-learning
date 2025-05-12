package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import org.apache.commons.lang3.BooleanUtils;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.dao.entity.*;
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
        respVo.setTeacherLanguage(studentClass.getTeacherLanguage());
        respVo.setTextbookId(studentClass.getCourseId());
        respVo.setTextbookName(studentClass.getTextbook());
        respVo.setCourseTime(studentClass.getCourseTime().toString() + " " + studentClass.getBeginTime() + "-" + studentClass.getEndTime());
        respVo.setCourseType(studentClass.getCourseType());
        if (studentClass.getStudentCourseStatus() != null) {
            respVo.setStudentCourseStatusContent(Objects.requireNonNull(CourseStatusEnum.getCourseStatusByType(studentClass.getStudentCourseStatus())).getEntRemark());
        }
        if (studentClass.getTeacherCourseStatus() != null) {
            respVo.setTeacherCourseStatusContent(Objects.requireNonNull(CourseStatusEnum.getCourseStatusByType(studentClass.getTeacherCourseStatus())).getEntRemark());
        }
        respVo.setCourseVideoUrl(studentClass.getCourseVideoUrl());
        //默认25分钟  团体课1小时
        respVo.setCourseLongTime(new BigDecimal(25));
        if (StringUtils.equals(studentClass.getCourseType(), CourseTypeEnum.GROUP.name())) {
            respVo.setCourseLongTime(new BigDecimal(60));
        }
        respVo.setIsCourseVideoExpired(studentClass.getIsCourseVideoExpired());
        respVo.setAffiliateId(studentClass.getAffiliateId());
        respVo.setAffiliateName(studentClass.getAffiliateName());
        respVo.setIsCanComplaint(BooleanUtils.isFalse(studentClass.getIsComplaint()));
        respVo.setIsCanEvaluation(BooleanUtils.isFalse(studentClass.getIsEvaluation()));
        respVo.setIsCanCancelComplaint(BooleanUtils.isTrue(studentClass.getIsComplaint()));
        //3小时以外才能变更课程时间
        Date classTime = DateUtil.parse(studentClass.getCourseTime() + " " + studentClass.getBeginTime(), "yyyy-MM-dd HH:mm");
        long differenceInMillis = classTime.getTime() - new Date().getTime();
        long differenceInHours = differenceInMillis / (60 * 60 * 1000);
        respVo.setIsCanUpdateTime(differenceInHours > 3600);
        //时间未到才能取消课程
        respVo.setIsCanCenterClass(Objects.equals(studentClass.getClassStatus(), CourseStatusEnum.NOT_STARTED.getStatus()) && differenceInHours > 0);
        return respVo;
    }

    default StudentClass toCreate(String userCode, String userName, StudentClassAddReqVo reqVo, Student student, Teacher teacher, Affiliate affiliate, UserFinance studentFinance) {
        StudentClass studentClass = new StudentClass();
        studentClass.setDeleted(false);
        studentClass.setCreator(userCode);
        studentClass.setCreateName(userName);
        studentClass.setCreateTime(new Date());
        studentClass.setRecordId(UUID.randomUUID().toString());
        studentClass.setTeacherCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setStudentCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setClassStatus(CourseStatusEnum.NOT_STARTED.getStatus());

        studentClass.setCourseVideoUrl(null);
        studentClass.setZoomId(null);
        studentClass.setIsCourseVideoExpired(false);
        studentClass.setStudentId(student.getRecordId());
        studentClass.setStudentName(student.getName());
        studentClass.setStudentCountry(student.getCountry());
        studentClass.setTeacherId(teacher.getRecordId());
        studentClass.setTeacherName(teacher.getName());
        studentClass.setTeacherCountry(teacher.getCountry());
        if (reqVo.getCourseType() != null) {
            CourseTypeEnum courseTypeEnum = CourseTypeEnum.valueOf(reqVo.getCourseType());
            studentClass.setCourseType(courseTypeEnum.name());
        }
        studentClass.setTeacherLanguage(teacher.getLanguage());
        studentClass.setCourseTime(DateUtil.parse(reqVo.getCourseDate(), "yyyy-MM-dd"));
        if (StringUtils.isNotEmpty(reqVo.getCourseTime())) {
            String[] arr = StringUtils.split(reqVo.getCourseTime(), "-");
            studentClass.setBeginTime(arr[0]);
            studentClass.setEndTime(arr[1]);
        }
        if (affiliate != null) {
            studentClass.setAffiliateId(affiliate.getRecordId());
            studentClass.setAffiliateName(affiliate.getName());
        }
        studentClass.setStudentConsumption(BigDecimalUtil.nullOrZero(studentFinance.getConsumptionQty()));
        studentClass.setEfficientDate(studentFinance.getExpirationTime());
        studentClass.setStudentBalance(BigDecimalUtil.nullOrZero(studentFinance.getBalanceQty()));
        studentClass.setIsEvaluation(false);
        studentClass.setIsComplaint(false);
        studentClass.setCourseId(reqVo.getTextbookId());
        studentClass.setCourseName(reqVo.getTextbookName());
        studentClass.setPrice(teacher.getPrice());
        studentClass.setCreditsPrice(teacher.getCreditsPrice());
        studentClass.setGroupPrice(teacher.getGroupPrice());
        studentClass.setCoin(teacher.getCoin());
        studentClass.setGroupCoin(teacher.getGroupCoin());
        return studentClass;
    }

    default TeacherEvaluationRecord toCreateTeacherEvaluationRecord(String userCode, BigDecimal rating, String remark, StudentClass studentClass) {
        TeacherEvaluationRecord teacherEvaluationRecord = new TeacherEvaluationRecord();
        teacherEvaluationRecord.setCreator(userCode);
        teacherEvaluationRecord.setCreateTime(new Date());
        teacherEvaluationRecord.setRecordId(UUID.randomUUID().toString());
        teacherEvaluationRecord.setTeacherId(studentClass.getTeacherId());
        teacherEvaluationRecord.setTeacherName(studentClass.getTeacherName());
        teacherEvaluationRecord.setTeacherEmail(studentClass.getTeacherEmail());
        teacherEvaluationRecord.setStudentId(studentClass.getStudentId());
        teacherEvaluationRecord.setStudentName(studentClass.getStudentName());
        teacherEvaluationRecord.setStudentEmail(studentClass.getStudentEmail());
        teacherEvaluationRecord.setStudentClassId(studentClass.getRecordId());
        teacherEvaluationRecord.setStudentClassDate(studentClass.getCourseTime());
        teacherEvaluationRecord.setStudentCourse(studentClass.getCourseName());
        teacherEvaluationRecord.setStudentClassBeginTime(studentClass.getBeginTime());
        teacherEvaluationRecord.setStudentClassEndTime(studentClass.getEndTime());
        teacherEvaluationRecord.setRating(rating);
        teacherEvaluationRecord.setRemark(remark);
        return teacherEvaluationRecord;
    }

    default TeacherComplaintRecord toCreateTeacherComplaintRecord(String userCode, BigDecimal amount, String remark, StudentClass studentClass) {
        TeacherComplaintRecord teacherComplaintRecord = new TeacherComplaintRecord();
        teacherComplaintRecord.setCreator(userCode);
        teacherComplaintRecord.setCreateTime(new Date());
        teacherComplaintRecord.setRecordId(UUID.randomUUID().toString());
        teacherComplaintRecord.setTeacherId(studentClass.getTeacherId());
        teacherComplaintRecord.setTeacherName(studentClass.getTeacherName());
        teacherComplaintRecord.setTeacherEmail(studentClass.getTeacherEmail());
        teacherComplaintRecord.setStudentId(studentClass.getStudentId());
        teacherComplaintRecord.setStudentName(studentClass.getStudentName());
        teacherComplaintRecord.setStudentEmail(studentClass.getStudentEmail());
        teacherComplaintRecord.setStudentClassId(studentClass.getRecordId());
        teacherComplaintRecord.setStudentClassDate(studentClass.getCourseTime());
        teacherComplaintRecord.setStudentClassBeginTime(studentClass.getBeginTime());
        teacherComplaintRecord.setStudentClassEndTime(studentClass.getEndTime());
        teacherComplaintRecord.setAmount(amount);
        teacherComplaintRecord.setUsedAmount(amount);
        teacherComplaintRecord.setIsCancel(false);
        teacherComplaintRecord.setRemark(remark);
        return teacherComplaintRecord;
    }


    default StudentClass toCreateByRegular(String userCode, String userName, StudentClassRegular studentClassRegular, StudentClassRegularRecord studentClassRegularRecord, Student student, Teacher teacher, Affiliate affiliate, UserFinance studentFinance) {
        StudentClass studentClass = new StudentClass();
        studentClass.setDeleted(false);
        studentClass.setCreator(userCode);
        studentClass.setCreateName(userName);
        studentClass.setCreateTime(new Date());
        studentClass.setRecordId(UUID.randomUUID().toString());
        studentClass.setTeacherCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setStudentCourseStatus(CourseStatusEnum.NOT_STARTED.getStatus());
        studentClass.setClassStatus(CourseStatusEnum.NOT_STARTED.getStatus());

        studentClass.setCourseVideoUrl(null);
        studentClass.setZoomId(null);
        studentClass.setIsCourseVideoExpired(false);
        studentClass.setStudentId(student.getRecordId());
        studentClass.setStudentName(student.getName());
        studentClass.setStudentCountry(student.getCountry());
        studentClass.setTeacherId(teacher.getRecordId());
        studentClass.setTeacherName(teacher.getName());
        studentClass.setTeacherCountry(teacher.getCountry());
        if (studentClassRegular.getCourseType() != null) {
            CourseTypeEnum courseTypeEnum = CourseTypeEnum.valueOf(studentClassRegular.getCourseType());
            assert courseTypeEnum != null;
            studentClass.setCourseType(courseTypeEnum.name());
        }
        studentClass.setCourseTime(studentClassRegularRecord.getCourseTime());
        studentClass.setBeginTime(studentClassRegular.getBeginTime());
        studentClass.setEndTime(studentClassRegular.getEndTime());
        if (affiliate != null) {
            studentClass.setAffiliateId(affiliate.getRecordId());
            studentClass.setAffiliateName(affiliate.getName());
        }
        studentClass.setStudentConsumption(BigDecimalUtil.nullOrZero(studentFinance.getConsumptionQty()));
        studentClass.setEfficientDate(studentFinance.getExpirationTime());
        studentClass.setStudentBalance(BigDecimalUtil.nullOrZero(studentFinance.getBalanceQty()));
        studentClass.setIsEvaluation(false);
        studentClass.setIsComplaint(false);
        studentClass.setCourseId(studentClassRegular.getCourseId());
        studentClass.setCourseName(studentClassRegular.getCourseName());
        studentClass.setPrice(teacher.getPrice());
        studentClass.setCreditsPrice(teacher.getCreditsPrice());
        return studentClass;
    }

}
