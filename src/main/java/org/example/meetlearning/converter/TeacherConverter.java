package org.example.meetlearning.converter;

import cn.hutool.core.util.BooleanUtil;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.enums.GenderEnum;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.shared.teacher.SharedTeacherListRespVo;
import org.example.meetlearning.vo.teacher.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Mapper
public interface TeacherConverter {

    TeacherConverter INSTANCE = Mappers.getMapper(TeacherConverter.class);

    default TeacherListRespVo toListVo(Teacher teacher) {
        TeacherListRespVo respVo = new TeacherListRespVo();
        respVo.setRecordId(teacher.getRecordId());
        respVo.setName(teacher.getName());
        respVo.setAttendance(teacher.getAttendance());
        respVo.setRating(teacher.getRating());
        respVo.setPrice(teacher.getPrice());
        respVo.setCreditsPrice(teacher.getCreditsPrice());
        respVo.setRate(teacher.getRate());
        respVo.setConfirmed(teacher.getConfirmedQty());
        respVo.setCanceled(teacher.getCanceledQty());
        respVo.setComplaints(teacher.getComplaintsQty());
        respVo.setSalary(teacher.getSalaryAmount());
        respVo.setTestStatus(BooleanUtil.isTrue(teacher.getTestStatus()));
        respVo.setManagerStatus(BooleanUtil.isTrue(teacher.getManagerStatus()));
        return respVo;
    }

    default Teacher toCreateTeacher(String userCode, String userName, TeacherAddReqVo reqVo) {
        Teacher teacher = new Teacher();
        teacher.setDeleted(false);
        teacher.setCreator(userCode);
        teacher.setCreateName(userName);
        teacher.setCreateTime(new Date());
        teacher.setRecordId(UUID.randomUUID().toString());
        teacher.setEnName(reqVo.getEnName());
        teacher.setEmail(reqVo.getEmail());
        teacher.setCountry(reqVo.getCountry());
        teacher.setManagerId(reqVo.getManagerId());
        teacher.setManager(reqVo.getManager());
        return teacher;
    }


    default Teacher toUpdateTeacher(String userCode, String userName, Teacher teacher, TeacherUpdateReqVo reqVo) {
        teacher.setUpdator(userCode);
        teacher.setUpdateName(userName);
        teacher.setName(reqVo.getName());
        teacher.setPrice(reqVo.getPrice());
        teacher.setCreditsPrice(reqVo.getCreditsPrice());
        teacher.setRate(reqVo.getRate());
        teacher.setEmail(reqVo.getEmail());
        teacher.setCountry(reqVo.getCountry());
        teacher.setManagerId(reqVo.getManagerId());
        teacher.setManager(reqVo.getManager());
        teacher.setLanguage(reqVo.getLanguage());
        teacher.setCurrencyName(reqVo.getCurrencyCode());
        teacher.setCurrencyName(reqVo.getCurrencyName());
//        teacher.setSpecialties(reqVo.getSpecialties());
        return teacher;
    }

    default TeacherLastCommentRespVo toCommentVo(String userCode, String userName) {
        TeacherLastCommentRespVo commentVo = new TeacherLastCommentRespVo();
        commentVo.setRecordId(UUID.randomUUID().toString());
        commentVo.setCreator(userCode);
        commentVo.setCreateName(userName);
        commentVo.setComment("测试评论");
        commentVo.setCreateTime(new Date());
        commentVo.setTeacherId(UUID.randomUUID().toString());
        commentVo.setCourseId(UUID.randomUUID().toString());
        commentVo.setCourseName("测试课程");
        return commentVo;
    }

    default SharedTeacherListRespVo toSharedTeacherListRespVo(Teacher teacher) {
        SharedTeacherListRespVo respVo = new SharedTeacherListRespVo();
        respVo.setRecordId(teacher.getRecordId());
        respVo.setName(teacher.getName());
        respVo.setCountry(teacher.getCountry());
        respVo.setPrice(BigDecimalUtil.nullOrZero(teacher.getPrice()));
        respVo.setCreditsPrice(BigDecimalUtil.nullOrZero(teacher.getCreditsPrice()));
        respVo.setVideoUrl(teacher.getVideoUrl());
        respVo.setAvatarUtl(teacher.getAvatarUrl());
        return respVo;
    }


    default TeacherFeature toTeacherFeature(String userCode, String teacherId, String feature) {
        TeacherFeature teacherFeature = new TeacherFeature();
        teacherFeature.setTeacherId(teacherId);
        teacherFeature.setRecordId(UUID.randomUUID().toString());
        teacherFeature.setCreateTime(new Date());
        teacherFeature.setCreator(userCode);
        teacherFeature.setSpecialists(feature);
        return teacherFeature;
    }

    default TeacherInfoRespVo toTeacherInfo(Teacher teacher) {
        TeacherInfoRespVo teacherRespVo = new TeacherInfoRespVo();
        teacherRespVo.setRecordId(teacher.getRecordId());
        teacherRespVo.setName(teacher.getName());
        teacherRespVo.setEnName(teacher.getEnName());
        teacherRespVo.setAttendance(teacher.getAttendance());
        teacherRespVo.setRating(teacher.getRating());
        teacherRespVo.setPrice(teacher.getPrice());
        teacherRespVo.setCreditsPrice(teacher.getCreditsPrice());
        teacherRespVo.setRate(teacher.getRate());
        teacherRespVo.setConfirmedQty(teacher.getConfirmedQty());
        teacherRespVo.setCanceledQty(teacher.getCanceledQty());
        teacherRespVo.setComplaintsQty(teacher.getComplaintsQty());
        teacherRespVo.setSalaryAmount(teacher.getSalaryAmount());
        teacherRespVo.setEmail(teacher.getEmail());
        teacherRespVo.setCountry(teacher.getCountry());
        teacherRespVo.setManagerId(teacher.getManagerId());
        teacherRespVo.setManager(teacher.getManager());
        teacherRespVo.setAvatarUrl(teacher.getAvatarUrl());
        teacherRespVo.setLanguage(teacher.getLanguage());
        teacherRespVo.setCurrencyCode(teacher.getCurrencyCode());
        teacherRespVo.setCurrencyName(teacher.getCurrencyName());
        teacherRespVo.setGender(GenderEnum.MALE.getEnName());
        teacherRespVo.setSpecialties(teacher.getSpecialties());
        teacherRespVo.setVideoUrl(teacher.getVideoUrl());
        teacherRespVo.setTestStatus(teacher.getTestStatus());
        teacherRespVo.setManagerStatus(teacher.getManagerStatus());
        teacherRespVo.setEnabledStatus(teacher.getEnabledStatus());
        teacherRespVo.setCreateTime(teacher.getCreateTime());
        teacherRespVo.setCreateName(teacher.getCreateName());
        teacherRespVo.setCreator(teacher.getCreator());
        return teacherRespVo;
    }

    default TeacherDashboardRespVo toTeacherDashboard(Teacher teacher) {
        TeacherDashboardRespVo respVo = new TeacherDashboardRespVo();
        respVo.setRate(BigDecimalUtil.nullOrZero(teacher.getRate()));
        respVo.setConfirmedQty(BigDecimalUtil.nullOrZero(teacher.getConfirmedQty()));
        respVo.setCancelledQty(BigDecimalUtil.nullOrZero(teacher.getCanceledQty()));
        respVo.setComplaintsQty(BigDecimalUtil.nullOrZero(teacher.getComplaintsQty()));
        respVo.setConfirmedClassesAmount(BigDecimalUtil.nullOrZero(teacher.getSalaryAmount()));
        respVo.setCancelledDeductionsAmount(BigDecimal.ZERO);
        respVo.setComplaintDeductionsAmount(BigDecimal.ZERO);
        respVo.setBonus(BigDecimal.ZERO);
        respVo.setCommission(BigDecimal.ZERO);
        respVo.setTotalSalary(BigDecimalUtil.nullOrZero(teacher.getSalaryAmount()));
        respVo.setAttendanceRate(BigDecimalUtil.nullOrZero(teacher.getAttendance()));
        respVo.setRating(BigDecimalUtil.nullOrZero(teacher.getRating()));
        return respVo;
    }

}
