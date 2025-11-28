package org.example.meetlearning.converter;

import cn.hutool.core.util.BooleanUtil;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherEvaluationRecord;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.enums.CurrencyEnum;
import org.example.meetlearning.enums.GenderEnum;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.SelectValueVo;
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
        respVo.setId(teacher.getId());
        respVo.setRecordId(teacher.getRecordId());
        respVo.setName(teacher.getName());
        respVo.setAttendance(teacher.getAttendance());
        respVo.setRating(teacher.getRating());
        respVo.setPrice(teacher.getPrice());
        respVo.setCoin(teacher.getCoin());
        respVo.setGroupPrice(teacher.getGroupPrice());
        respVo.setGroupCoin(teacher.getGroupCoin());
        respVo.setCreditsPrice(teacher.getCreditsPrice());
        respVo.setRate(teacher.getRate());
        respVo.setConfirmed(teacher.getConfirmedQty());
        respVo.setCanceled(teacher.getCanceledQty());
        respVo.setComplaints(teacher.getComplaintsQty());
        respVo.setSalary(teacher.getSalaryAmount());
        respVo.setTestStatus(BooleanUtil.isTrue(teacher.getTestStatus()));
        respVo.setGroupStatus(BooleanUtil.isTrue(teacher.getGroupStatus()));
        respVo.setManagerStatus(BooleanUtil.isTrue(teacher.getManagerStatus()));
        respVo.setEnableStatus(BooleanUtil.isTrue(teacher.getEnabledStatus()));
        respVo.setCountry(teacher.getCountry());
        respVo.setManager(teacher.getManager());
        respVo.setManagerId(teacher.getManagerId());
        respVo.setRole(BooleanUtil.isTrue(teacher.getManagerStatus()) ? "Manager" : "Teacher");
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
        teacher.setName(reqVo.getEnName());
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
        teacher.setGroupPrice(reqVo.getGroupPrice());
        teacher.setCreditsPrice(reqVo.getCreditsPrice());
        teacher.setCoin(reqVo.getCoin());
        teacher.setGroupCoin(reqVo.getGroupCoin());
        teacher.setEmail(reqVo.getEmail());
        teacher.setCountry(reqVo.getCountry());
        teacher.setManagerId(reqVo.getManagerId());
        teacher.setManager(reqVo.getManager());
        teacher.setLanguage(reqVo.getLanguage());
        teacher.setCurrencyCode(reqVo.getCurrencyCode());
        teacher.setGender(reqVo.getGender());
        teacher.setMeetLink(reqVo.getMeetLink());
        teacher.setMeetPassWord(reqVo.getMeetPassWord());
        return teacher;
    }

    default TeacherLastCommentRespVo toCommentVo(TeacherEvaluationRecord evaluationRecord) {
        TeacherLastCommentRespVo commentVo = new TeacherLastCommentRespVo();
        commentVo.setRecordId(evaluationRecord.getRecordId());
        commentVo.setCreator(evaluationRecord.getCreator());
        commentVo.setCreateName(evaluationRecord.getStudentName());
        commentVo.setComment(evaluationRecord.getRemark());
        commentVo.setCreateTime(evaluationRecord.getCreateTime());
        commentVo.setTeacherId(evaluationRecord.getTeacherId());
        commentVo.setCourseId(evaluationRecord.getClassId()+"");
        commentVo.setCourseName(evaluationRecord.getClassId()+"");
        return commentVo;
    }
    default TeacherEvaluationRecordRespVo toEvaluationRecordVo(TeacherEvaluationRecord evaluationRecord) {
        TeacherEvaluationRecordRespVo commentVo = new TeacherEvaluationRecordRespVo();
        commentVo.setRecordId(evaluationRecord.getRecordId());
        commentVo.setStudentId(evaluationRecord.getStudentId());
        commentVo.setStudentName(evaluationRecord.getStudentName());
        commentVo.setStudentEmail(evaluationRecord.getStudentEmail());
        commentVo.setRating(evaluationRecord.getRating());
        commentVo.setRemark(evaluationRecord.getRemark());
        commentVo.setClassId(evaluationRecord.getClassId());
        return commentVo;
    }

    default SharedTeacherListRespVo toSharedTeacherListRespVo(Teacher teacher) {
        SharedTeacherListRespVo respVo = new SharedTeacherListRespVo();
        respVo.setRecordId(teacher.getRecordId());
        respVo.setName(teacher.getName());
        respVo.setCountry(teacher.getCountry());
        respVo.setCoin(BigDecimalUtil.nullOrZero(teacher.getCoin()));
        respVo.setGroupCoin(BigDecimalUtil.nullOrZero(teacher.getGroupCoin()));
        respVo.setCreditsPrice(BigDecimalUtil.nullOrZero(teacher.getCreditsPrice()));
        respVo.setVideoUrl(teacher.getVideoUrl());
        respVo.setAvatarUtl(teacher.getAvatarUrl());
        return respVo;
    }


    default TeacherFeature toTeacherFeature(String userCode, String teacherId, Textbook textbook) {
        TeacherFeature teacherFeature = new TeacherFeature();
        teacherFeature.setTeacherId(teacherId);
        teacherFeature.setRecordId(UUID.randomUUID().toString());
        teacherFeature.setCreateTime(new Date());
        teacherFeature.setCreator(userCode);
        teacherFeature.setTextbookId(textbook.getRecordId());
        teacherFeature.setTextbookName(textbook.getName());
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
        teacherRespVo.setGroupPrice(teacher.getGroupPrice());
        teacherRespVo.setCoin(teacher.getCoin());
        teacherRespVo.setGroupCoin(teacher.getGroupCoin());
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
        respVo.setRate(BigDecimalUtil.nullOrZero(teacher.getPrice()));
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
        respVo.setCurrencyCode(teacher.getCurrencyCode());
        respVo.setCurrencyName(teacher.getCurrencyName());
        return respVo;
    }

}
