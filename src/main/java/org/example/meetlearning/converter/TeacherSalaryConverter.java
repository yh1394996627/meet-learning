package org.example.meetlearning.converter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.teacher.TeacherSalaryRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;


@Mapper
public interface TeacherSalaryConverter {

    TeacherSalaryConverter INSTANCE = Mappers.getMapper(TeacherSalaryConverter.class);

    default TeacherSalary toCreate(String userCode, String userName, Teacher teacher) {
        TeacherSalary teacherSalary = new TeacherSalary();
        teacherSalary.setDeleted(false);
        teacherSalary.setCreator(userCode);
        teacherSalary.setCreateName(userName);
        teacherSalary.setCreateTime(new Date());
        teacherSalary.setRecordId(UUID.randomUUID().toString());
        teacherSalary.setTeacherId(teacher.getRecordId());
        teacherSalary.setIsVerification(false);
        teacherSalary.setPrice(teacher.getPrice());
        teacherSalary.setGroupPrice(teacher.getGroupPrice());
        teacherSalary.setConfirmedQty(BigDecimal.ZERO);
        teacherSalary.setOneStarQty(BigDecimal.ZERO);
        teacherSalary.setAbsentQty(BigDecimal.ZERO);
        teacherSalary.setGroupConfirmedQty(BigDecimal.ZERO);
        teacherSalary.setGroupOneStarQty(BigDecimal.ZERO);
        teacherSalary.setGroupAbsentQty(BigDecimal.ZERO);
        teacherSalary.setBeginDate(new Date());
        teacherSalary.setEndDate(null);
        teacherSalary.setConfirmedAmount(BigDecimal.ZERO);
        teacherSalary.setOneStarAmount(BigDecimal.ZERO);
        teacherSalary.setAbsentAmount(BigDecimal.ZERO);
        teacherSalary.setCurrencyCode(teacherSalary.getCurrencyCode());
        return teacherSalary;
    }

    default TeacherSalaryRespVo toRespVo(TeacherSalary teacherSalary) {
        TeacherSalaryRespVo respVo = new TeacherSalaryRespVo();
        respVo.setRecordId(teacherSalary.getRecordId());
        respVo.setTeacherId(teacherSalary.getTeacherId());
        respVo.setIsVerification(teacherSalary.getIsVerification());
        respVo.setCurrencyCode(teacherSalary.getCurrencyCode());
        respVo.setSettlementDate(teacherSalary.getEndDate());
        BigDecimal amount = BigDecimalUtil.sub(BigDecimalUtil.sub(BigDecimalUtil.sub(teacherSalary.getConfirmedAmount(), teacherSalary.getOneStarAmount()), teacherSalary.getAbsentAmount()), teacherSalary.getDeductionAmount());
        respVo.setSalary(amount);
        return respVo;
    }
}
