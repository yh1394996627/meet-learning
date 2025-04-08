package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;


import cn.hutool.core.date.DateUtil;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherClassRemark;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.remark.TeacherClassRemarkPageRespVo;
import org.example.meetlearning.vo.remark.TeacherClassRemarkReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeacherClassRemarkConverter {

    TeacherClassRemarkConverter INSTANCE = Mappers.getMapper(TeacherClassRemarkConverter.class);


    default TeacherClassRemark toCreate(String userCode, String userName, TeacherClassRemarkReqVo reqVo, StudentClass studentClass) {
        TeacherClassRemark remark = new TeacherClassRemark();
        remark.setCreator(userCode);
        remark.setCreateName(userName);
        remark.setCreateTime(new Date());
        remark.setRecordId(UUID.randomUUID().toString());
        remark.setClassId(studentClass.getRecordId());
        remark.setTeacherId(studentClass.getTeacherId());
        remark.setStudentId(studentClass.getStudentId());
        remark.setFilePage(reqVo.getFilePage());
        remark.setClassRemark(reqVo.getClassRemark());
        remark.setRemark(reqVo.getRemark());
        remark.setTeacherName(studentClass.getTeacherName());
        remark.setClassTime(DateUtil.parse(studentClass.getCourseTime() + " " + studentClass.getBeginTime()));
        remark.setTextbook(studentClass.getTextbook());
        return remark;
    }

    default TeacherClassRemarkPageRespVo toRespVo(TeacherClassRemark remark) {
        TeacherClassRemarkPageRespVo respVo = new TeacherClassRemarkPageRespVo();
        respVo.setRecordId(remark.getRecordId());
        respVo.setTeacherId(remark.getTeacherId());
        respVo.setTeacherName(remark.getTeacherName());
        respVo.setFilePage(BigDecimalUtil.nullOrZero(remark.getFilePage()));
        respVo.setClassRemark(remark.getClassRemark());
        respVo.setRemark(remark.getRemark());
        return respVo;
    }


}
