package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.dao.mapper.TeacherFeatureMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
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

    /**
     * 根据老师id批量查询
     */
    public List<TeacherFeature> selectByTeacherIds(List<String> teacherIds) {
        if(CollectionUtils.isEmpty(teacherIds)){
            return new ArrayList<>();
        }
        return teacherFeatureMapper.selectByTeacherIds(teacherIds);
    }

    /**
     * 根据特点查询老师
     */
    public List<String> selectTeacherIdBySpecialists(String specialists, List<String> teacherIds) {
        return teacherFeatureMapper.selectTeacherIdByTextbookName(specialists, teacherIds);
    }


}
