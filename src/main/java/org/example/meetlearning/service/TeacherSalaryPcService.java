package org.example.meetlearning.service;


import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.converter.TeacherSalaryConverter;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.TeacherSalaryService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.StudentClassPriceGroupVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.teacher.TeacherSalaryRespVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
@Transactional
public class TeacherSalaryPcService {

    private TeacherSalaryService teacherSalaryService;

    private StudentClassService studentClassService;

    private TeacherService teacherService;

    public void updateSalary(String userCode, String userName, String teacherId, Date endDate) {
        Assert.isTrue(StringUtils.hasText(teacherId), "teacherId cannot be empty");
        TeacherSalary teacherSalary = teacherSalaryService.selectByUnVerTeacherId(teacherId);
        if (teacherSalary == null) {
            Teacher teacher = teacherService.selectByRecordId(teacherId);
            Assert.notNull(teacher, "teacher cannot be empty");
            teacherSalary = TeacherSalaryConverter.INSTANCE.toCreate(userCode, userName, teacher);
            teacherSalaryService.insertEntity(teacherSalary);
        }
        List<StudentClassPriceGroupVo> studentClassPriceGroupVos = studentClassService.selectByGltDateTeacherId(teacherId, DateUtil.parseDate(DateUtil.formatDate(endDate)));
        //获取 确认数量 和 确认金额
        List<StudentClassPriceGroupVo> completeVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.FINISH.getStatus())).toList();
        BigDecimal completeQty = completeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeGroupQty = completeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeAmount = completeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeGroupAmount = completeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setConfirmedQty(completeQty);
        teacherSalary.setGroupConfirmedQty(completeGroupQty);
        teacherSalary.setConfirmedAmount(BigDecimalUtil.add(completeAmount, completeGroupAmount));
        //获取 1星数量 和 1星金额
        List<StudentClassPriceGroupVo> oneStarVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.ONT_STAR.getStatus())).toList();
        BigDecimal oneStarQty = oneStarVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarGroupQty = oneStarVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarAmount = oneStarVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarGroupAmount = oneStarVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setOneStarQty(oneStarQty);
        teacherSalary.setGroupOneStarQty(oneStarGroupQty);
        teacherSalary.setOneStarAmount(BigDecimalUtil.add(oneStarAmount, oneStarGroupAmount));
        //获取 缺席数量 和 缺席金额
        List<StudentClassPriceGroupVo> absentVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.ABSENT.getStatus())).toList();
        BigDecimal absentQty = absentVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentGroupQty = absentVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentAmount = absentVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentGroupAmount = absentVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice())).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setAbsentQty(absentQty);
        teacherSalary.setGroupAbsentQty(absentGroupQty);
        teacherSalary.setAbsentAmount(BigDecimalUtil.add(absentAmount, absentGroupAmount));
        teacherSalaryService.updateEntity(teacherSalary);
        //获取 三天内取消的课程数量 和 金额 薪资一半
        List<StudentClassPriceGroupVo> cancelDeVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.CANCEL_DE.getStatus())).toList();
        BigDecimal cancelDeQty = cancelDeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeGroupQty = cancelDeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeAmount = absentVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).divide(new BigDecimal(2), 2)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeGroupAmount = absentVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).divide(new BigDecimal(2), 2)).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setDeductionQty(cancelDeQty);
        teacherSalary.setGroupDeductionQtyQty(cancelDeGroupQty);
        teacherSalary.setDeductionAmount(BigDecimalUtil.add(cancelDeAmount, cancelDeGroupAmount));
        teacherSalaryService.updateEntity(teacherSalary);

    }


    public List<TeacherSalaryRespVo> settlementSalaryList(RecordIdQueryVo queryVo) {
        List<TeacherSalary> teacherSalaries = teacherSalaryService.selectByTeacherId(queryVo.getRecordId());
        teacherSalaries = teacherSalaries.stream().sorted(Comparator.comparing(TeacherSalary::getId)).toList();
        return teacherSalaries.stream().map(TeacherSalaryConverter.INSTANCE::toRespVo).toList();
    }


    public void settlementSalary(String userCode, String userName, String teacherId) {
        Date endDate = DateUtil.parseDate(DateUtil.formatDate(new Date()));
        updateSalary(userCode, userName, teacherId, DateUtil.parseDate(DateUtil.formatDate(new Date())));
        TeacherSalary teacherSalary = teacherSalaryService.selectByUnVerTeacherId(teacherId);
        teacherSalary.setIsVerification(true);
        //结算过的课程 核酸状态 改为true;
        studentClassService.updateByGltDateTeacherId(teacherId, endDate);
    }

}
