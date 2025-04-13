package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TeacherClassRemarkConverter;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.entity.TeacherClassRemark;
import org.example.meetlearning.service.impl.StudentClassService;
import org.example.meetlearning.service.impl.TeacherClassRemarkService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.remark.TeacherClassRemarkPageRespVo;
import org.example.meetlearning.vo.remark.TeacherClassRemarkQueryVo;
import org.example.meetlearning.vo.remark.TeacherClassRemarkReqVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class TeacherClassRemarkPcService {

    private TeacherClassRemarkService teacherClassRemarkService;

    private StudentClassService studentClassService;

    public PageVo<TeacherClassRemarkPageRespVo> selectPageByStudentId(TeacherClassRemarkQueryVo queryVo) {
        Page<TeacherClassRemark> page = teacherClassRemarkService.selectPageByStudentId(queryVo.getStudentId(), queryVo.getPageRequest());
        return PageVo.map(page, TeacherClassRemarkConverter.INSTANCE::toRespVo);
    }

    public void add(String userCode, String userName, TeacherClassRemarkReqVo queryVo) {
        StudentClass studentClass = studentClassService.selectByRecordId(queryVo.getRecordId());
        Assert.notNull(studentClass, "Course information not obtained");
        TeacherClassRemark remark = TeacherClassRemarkConverter.INSTANCE.toCreate(userCode, userName, queryVo, studentClass);
        teacherClassRemarkService.insertEntity(remark);
    }

}
