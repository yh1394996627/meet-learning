package org.example.meetlearning.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherClassRemark;
import org.example.meetlearning.dao.mapper.TeacherClassRemarkMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TeacherClassRemarkService {

    private TeacherClassRemarkMapper teacherClassRemarkMapper;

    public int insertEntity(TeacherClassRemark teacherClassRemark) {
        return teacherClassRemarkMapper.insert(teacherClassRemark);
    }

    public int updateEntity(TeacherClassRemark teacherClassRemark) {
        return teacherClassRemarkMapper.updateByPrimaryKeySelective(teacherClassRemark);
    }

    public Page<TeacherClassRemark> selectPageByStudentId(String studentId, Page<TeacherClassRemark> page) {
        return teacherClassRemarkMapper.selectPageByStudentId(studentId, page);
    }


}
