package org.example.meetlearning.dao.mapper;

import java.util.List;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.TeacherClassRemark;

public interface TeacherClassRemarkMapper {

    int insert(TeacherClassRemark record);

    Page<TeacherClassRemark> selectPageByStudentId(String studentId,Page<TeacherClassRemark> page);

    int updateByPrimaryKeySelective(TeacherClassRemark record);


}