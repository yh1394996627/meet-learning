package org.example.meetlearning.service;


import cn.hutool.core.date.DateUtil;
import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TeacherConverter;
import org.example.meetlearning.converter.TeacherSalaryConverter;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.entity.TeacherSalary;
import org.example.meetlearning.enums.CourseStatusEnum;
import org.example.meetlearning.enums.CourseTypeEnum;
import org.example.meetlearning.enums.LanguageContextEnum;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.TeacherSalaryService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.classes.StudentClassPriceGroupVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.teacher.TeacherDashboardRespVo;
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
public class TeacherSalaryPcService extends BasePcService {

    private TeacherSalaryService teacherSalaryService;

    private StudentClassService studentClassService;

    private TeacherService teacherService;

    public void updateSalary(String userCode, String userName, String teacherId, Date endDate) {
        Assert.isTrue(StringUtils.hasText(teacherId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        TeacherSalary teacherSalary = teacherSalaryService.selectByUnVerTeacherId(teacherId);
        if (teacherSalary == null) {
            Teacher teacher = teacherService.selectByRecordId(teacherId);
            Assert.notNull(teacher, getHint(LanguageContextEnum.TEACHER_NOTNULL));
            teacherSalary = TeacherSalaryConverter.INSTANCE.toCreate(userCode, userName, teacher);
            teacherSalaryService.insertEntity(teacherSalary);
        }
        List<StudentClassPriceGroupVo> studentClassPriceGroupVos = studentClassService.selectByGltDateTeacherId(teacherId, DateUtil.parseDate(DateUtil.formatDate(endDate)));
        List<StudentClassPriceGroupVo> completeVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.FINISH.getStatus())).toList();
        BigDecimal completeQty = completeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeGroupQty = completeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeAmount = completeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal completeGroupAmount = completeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setConfirmedQty(completeQty);
        teacherSalary.setGroupConfirmedQty(completeGroupQty);
        teacherSalary.setConfirmedAmount(BigDecimalUtil.add(completeAmount, completeGroupAmount));
        List<StudentClassPriceGroupVo> oneStarVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.ONT_STAR.getStatus())).toList();
        BigDecimal oneStarQty = oneStarVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarGroupQty = oneStarVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarAmount = oneStarVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal oneStarGroupAmount = oneStarVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setOneStarQty(oneStarQty);
        teacherSalary.setGroupOneStarQty(oneStarGroupQty);
        teacherSalary.setOneStarAmount(BigDecimalUtil.add(oneStarAmount, oneStarGroupAmount));
        List<StudentClassPriceGroupVo> absentVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.ABSENT.getStatus())).toList();
        BigDecimal absentQty = absentVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentGroupQty = absentVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentAmount = absentVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal absentGroupAmount = absentVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty()))).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setAbsentQty(absentQty);
        teacherSalary.setGroupAbsentQty(absentGroupQty);
        teacherSalary.setAbsentAmount(BigDecimalUtil.add(absentAmount, absentGroupAmount));
        teacherSalaryService.updateEntity(teacherSalary);
        List<StudentClassPriceGroupVo> cancelDeVos = studentClassPriceGroupVos.stream().filter(item -> Objects.equals(item.getClassStatus(), CourseStatusEnum.CANCEL_DE.getStatus())).toList();
        BigDecimal cancelDeQty = cancelDeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeGroupQty = cancelDeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getTotalQty())).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeAmount = cancelDeVos.stream().filter(f -> !StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty())).divide(new BigDecimal(2), 2)).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal cancelDeGroupAmount = cancelDeVos.stream().filter(f -> StringUtils.pathEquals(f.getCourseType(), CourseTypeEnum.GROUP.name())).map(item -> BigDecimalUtil.nullOrZero(item.getGroupPrice()).multiply(BigDecimalUtil.nullOrOne(item.getTotalQty())).divide(new BigDecimal(2), 2)).reduce(BigDecimal.ZERO, BigDecimal::add);
        teacherSalary.setDeductionQty(cancelDeQty);
        teacherSalary.setGroupDeductionQtyQty(cancelDeGroupQty);
        teacherSalary.setDeductionAmount(BigDecimalUtil.add(cancelDeAmount, cancelDeGroupAmount));
        teacherSalary.setEndDate(endDate);
        teacherSalaryService.updateEntity(teacherSalary);
    }


    public List<TeacherSalaryRespVo> settlementSalaryList(RecordIdQueryVo queryVo) {
        Assert.isTrue(StringUtils.hasText(queryVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        List<TeacherSalary> teacherSalaries = teacherSalaryService.selectByTeacherId(queryVo.getRecordId());
        teacherSalaries = teacherSalaries.stream().sorted(Comparator.comparing(TeacherSalary::getId)).toList();
        return teacherSalaries.stream().map(TeacherSalaryConverter.INSTANCE::toRespVo).toList();
    }


    public void settlementSalary(String userCode, String userName, String teacherId, String endDateStr, BigDecimal totalSalary) {
        Assert.isTrue(StringUtils.hasText(teacherId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        // 结算截止日期: 优先用前端弹窗选择的范围结束日, 未传则用当天(老逻辑)
        Date endDate = StringUtils.hasText(endDateStr)
                ? DateUtil.parseDate(endDateStr)
                : DateUtil.parseDate(DateUtil.formatDate(new Date()));
        updateSalary(userCode, userName, teacherId, endDate);
        TeacherSalary teacherSalary = teacherSalaryService.selectByUnVerTeacherId(teacherId);
        Assert.notNull(teacherSalary, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        if (totalSalary != null) {
            BigDecimal calculated = BigDecimalUtil.nullOrZero(teacherSalary.getConfirmedAmount())
                    .subtract(BigDecimalUtil.nullOrZero(teacherSalary.getDeductionAmount()))
                    .subtract(BigDecimalUtil.nullOrZero(teacherSalary.getAbsentAmount()))
                    .subtract(BigDecimalUtil.nullOrZero(teacherSalary.getOneStarAmount()));
            teacherSalary.setBonus(totalSalary.subtract(calculated));
        }
        teacherSalary.setIsVerification(true);
        teacherSalary.setEndDate(endDate);
        studentClassService.updateByGltDateTeacherId(teacherId, endDate);
        teacherSalaryService.updateEntity(teacherSalary);
    }


    public TeacherDashboardRespVo settlementPreview(String teacherId, String beginDate, String endDate) {
        Assert.isTrue(StringUtils.hasText(teacherId), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Teacher teacher = teacherService.selectByRecordId(teacherId);
        Assert.notNull(teacher, getHint(LanguageContextEnum.TEACHER_NOTNULL));
        TeacherDashboardRespVo respVo = TeacherConverter.INSTANCE.toTeacherDashboard(teacher);
        teacherSalaryService.selectByTeacherId(teacherId).stream()
                .filter(s -> Boolean.TRUE.equals(s.getIsVerification()) && s.getEndDate() != null)
                // 取最新一笔已结算记录的 end_date(而非所有记录中最大的 end_date)
                .max(Comparator.comparing(TeacherSalary::getId))
                .map(TeacherSalary::getEndDate)
                .ifPresent(d -> respVo.setLastSettlementDate(DateUtil.formatDate(d)));
        List<StudentClassPriceGroupVo> studentClassPriceGroupVos;
        if (StringUtils.hasText(beginDate) && StringUtils.hasText(endDate)) {
            studentClassPriceGroupVos = studentClassService.selectByDateRangeTeacherId(teacherId, beginDate, endDate);
        } else {
            studentClassPriceGroupVos = studentClassService.selectByGltDateTeacherId(teacherId, DateUtil.parseDate(DateUtil.formatDate(new Date())));
        }
        BigDecimal confirmedAmount = BigDecimal.ZERO;
        BigDecimal oneStarAmount = BigDecimal.ZERO;
        BigDecimal absentAmount = BigDecimal.ZERO;
        BigDecimal deductionAmount = BigDecimal.ZERO;
        BigDecimal confirmedQty = BigDecimal.ZERO;
        BigDecimal complaintsQty = BigDecimal.ZERO;
        BigDecimal absentQty = BigDecimal.ZERO;
        BigDecimal cancelledQty = BigDecimal.ZERO;
        for (StudentClassPriceGroupVo vo : studentClassPriceGroupVos) {
            boolean isGroup = StringUtils.pathEquals(vo.getCourseType(), CourseTypeEnum.GROUP.name());
            BigDecimal unit = isGroup ? BigDecimalUtil.nullOrZero(vo.getGroupPrice()) : BigDecimalUtil.nullOrZero(vo.getPrice());
            BigDecimal qty = BigDecimalUtil.nullOrZero(vo.getTotalQty());
            if (Objects.equals(vo.getClassStatus(), CourseStatusEnum.FINISH.getStatus())) {
                confirmedAmount = confirmedAmount.add(unit.multiply(qty));
                if (!isGroup) {
                    confirmedQty = confirmedQty.add(qty);
                }
            } else if (Objects.equals(vo.getClassStatus(), CourseStatusEnum.ONT_STAR.getStatus())) {
                oneStarAmount = oneStarAmount.add(unit.multiply(qty));
                if (!isGroup) {
                    complaintsQty = complaintsQty.add(qty);
                }
            } else if (Objects.equals(vo.getClassStatus(), CourseStatusEnum.ABSENT.getStatus())) {
                absentAmount = absentAmount.add(unit.multiply(qty));
                if (!isGroup) {
                    absentQty = absentQty.add(qty);
                }
            } else if (Objects.equals(vo.getClassStatus(), CourseStatusEnum.CANCEL_DE.getStatus())) {
                deductionAmount = deductionAmount.add(unit.multiply(qty).divide(new BigDecimal(2), 2));
            } else if (Objects.equals(vo.getClassStatus(), CourseStatusEnum.CANCEL.getStatus())) {
                cancelledQty = cancelledQty.add(qty);
            }
        }
        respVo.setConfirmedClassesAmount(confirmedAmount);
        respVo.setCancelledDeductionsAmount(deductionAmount);
        respVo.setComplaintDeductionsAmount(oneStarAmount);
        respVo.setAbsentAmount(absentAmount);
        respVo.setConfirmedQty(confirmedQty);
        respVo.setComplaintsQty(complaintsQty);
        respVo.setAbsentQty(absentQty);
        respVo.setCancelledQty(cancelledQty);
        respVo.setBonus(BigDecimal.ZERO);
        respVo.setCommission(BigDecimal.ZERO);
        return respVo;
    }

}
