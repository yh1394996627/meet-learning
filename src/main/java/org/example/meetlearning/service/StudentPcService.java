package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.StudentConverter;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.service.impl.StudentService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.Date;

@Service
@Slf4j
@AllArgsConstructor
public class StudentPcService {

    private final StudentService studentService;


    /**
     * 学生信息分页查询
     *
     * @param queryVo
     * @return
     */
    public RespVo<PageVo<StudentListRespVo>> studentPage(StudentRequestQueryVo queryVo) {
        Page<Student> page = studentService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        PageVo<StudentListRespVo> pageVo = PageVo.map(page, StudentConverter.INSTANCE::toStudentListRespVo);
        return new RespVo<>(pageVo);
    }

    public RespVo<String> studentAdd(String userCode, String userName, StudentAddReqVo reqVo) {
        try {
            Student student = StudentConverter.INSTANCE.toCreateStudent(userCode, userName, reqVo);
            studentService.save(student);
            return new RespVo<>("新增成功");
        } catch (Exception ex) {
            log.error("新增失败", ex);
            return new RespVo<>("新增失败", false, ex.getMessage());
        }
    }

    public RespVo<String> studentUpdate(String userCode, String userName, StudentUpdateReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId不能为空");
            Student student = studentService.findByRecordId(recordId);
            student = StudentConverter.INSTANCE.toUpdateStudent(student, reqVo);
            student.setUpdator(userCode);
            student.setUpdateName(userName);
            student.setUpdateTime(new Date());
            studentService.update(student);
            return new RespVo<>("更新成功");
        } catch (Exception ex) {
            log.error("更新失败", ex);
            return new RespVo<>("更新失败", false, ex.getMessage());
        }
    }

    public RespVo<String> deleteStudent(StudentRecordReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId不能为空");
            studentService.deletedByRecordId(recordId);
            return new RespVo<>("删除成功");
        } catch (Exception ex) {
            log.error("删除失败", ex);
            return new RespVo<>("删除失败", false, ex.getMessage());
        }
    }

    public RespVo<String> studentRemarkUpdate(StudentRemarkUpdateReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId不能为空");
            Student student = studentService.findByRecordId(recordId);
            student.setRemark(reqVo.getRemark());
            studentService.update(student);
            return new RespVo<>("更新备注成功");
        } catch (Exception ex) {
            log.error("更新备注失败", ex);
            return new RespVo<>("更新备注失败", false, ex.getMessage());
        }
    }
}
