package org.example.meetlearning.converter;

import java.math.BigDecimal;

import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.util.MD5Util;
import org.example.meetlearning.vo.teacher.TeacherAddReqVo;
import org.example.meetlearning.vo.teacher.TeacherListRespVo;
import org.example.meetlearning.vo.teacher.TeacherUpdateReqVo;
import org.example.meetlearning.vo.user.UserInfoRespVo;
import org.example.meetlearning.vo.user.UserManageOperaReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

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
        teacher.setAvatarUrl(reqVo.getAvatarUrl());
        teacher.setLanguage(reqVo.getLanguage());
        teacher.setCurrencyCode(reqVo.getCurrencyCode());
        teacher.setCurrencyName(reqVo.getCurrencySymbol());
//        teacher.setSpecialties(reqVo.getSpecialties());
        teacher.setVideoUrl(reqVo.getVideoUrl());
        return teacher;
    }

}
