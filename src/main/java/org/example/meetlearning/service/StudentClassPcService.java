package org.example.meetlearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.plexus.util.StringUtils;
import org.example.meetlearning.converter.StudentClassConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.service.impl.AffiliateService;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.StudentService;
import org.example.meetlearning.service.impl.TeacherService;
import org.example.meetlearning.vo.classes.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class StudentClassPcService {

    private final StudentClassService studentClassService;

    private final StudentService studentService;

    private final TeacherService teacherService;

    private final AffiliateService affiliateService;


    public RespVo<PageVo<StudentClassListRespVo>> studentClassPage(StudentClassQueryVo queryVo) {
        Page<StudentClass> page = studentClassService.selectByParams(queryVo.getParams(), queryVo.getPageRequest());
        PageVo<StudentClassListRespVo> pageVo = PageVo.map(page, StudentClassConverter.INSTANCE::toStudentClassListRespVo);
        return new RespVo<>(pageVo);
    }

    public RespVo<String> studentClassAdd(String entCode, String userCode, StudentClassAddReqVo reqVo) {
        try {
            //查询学生信息
            Student student = reqVo.getStudentId() != null ? studentService.findById(reqVo.getStudentId()) : null;
            Assert.notNull(student, "Student information not obtained");
            //查询老师信息
            Teacher teacher = reqVo.getTeacherId() != null ? teacherService.selectById(reqVo.getTeacherId()) : null;
            Assert.notNull(teacher, "Teacher information not obtained");
            //查询代理商信息
            Affiliate affiliate = null;
            if (student != null && StringUtils.isNotEmpty(student.getAffiliateId())) {
                affiliate = affiliateService.findByRecordId(student.getAffiliateId());
            }
            Assert.isTrue(reqVo.getCourseType() != null, "Teacher information not obtained");
            StudentClass studentClass = StudentClassConverter.INSTANCE.toCreate(entCode, userCode, reqVo, student, teacher, affiliate);
            studentClassService.insertEntity(studentClass);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>("Addition failed", false, ex.getMessage());
        }
    }


    public RespVo<List<SelectValueVo>> classCoinList() {
        List<BigDecimal> priceList = teacherService.priceGroupList();
        List<SelectValueVo> selectValueVos = priceList.stream().map(price -> new SelectValueVo(price.toString(), "Any teacher with " + price + " tokens")).toList();
        return new RespVo<>(selectValueVos);
    }


    public RespVo<List<SelectValueVo>> classTeacherList(StudentClassCommonQueryVo queryVo) {
        List<Teacher> teachers = teacherService.selectListParams(queryVo.getParams());
        List<SelectValueVo> selectValueVos = teachers.stream().map(teacher -> new SelectValueVo(teacher.getId().toString(), teacher.getName())).toList();
        return new RespVo<>(selectValueVos);
    }

    public RespVo<StudentClassTotalRespVo> classTotalList(StudentClassQueryVo queryVo) {
        Long cancelTotal = studentClassService.selectCancelByParams(queryVo.getParams());
        Long completeTotal = studentClassService.selectCompleteByParams(queryVo.getParams());
        return new RespVo<>(new StudentClassTotalRespVo(new BigDecimal(completeTotal), new BigDecimal(cancelTotal)));
    }

}
