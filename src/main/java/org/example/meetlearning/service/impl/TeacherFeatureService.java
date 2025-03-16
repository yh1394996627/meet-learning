package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.dao.mapper.TeacherFeatureMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class TeacherFeatureService {

    private final TeacherFeatureMapper teacherFeatureMapper;

    /**
     * 批量插入
     */
    public void insertBatch(List<TeacherFeature> record) {
        teacherFeatureMapper.insertBatch(record);
    }

    /**
     * 根据老师id删除
     */
    public void deleteByTeacherId(String teacherId) {
        teacherFeatureMapper.deletedByTeacherId(teacherId);
    }

    /**
     * 根据老师id查询
     */
    public List<TeacherFeature> selectByTeacherId(String teacherId) {
        return teacherFeatureMapper.selectByTeacherId(teacherId);
    }


}
