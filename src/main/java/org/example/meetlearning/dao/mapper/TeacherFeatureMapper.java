package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.TeacherFeature;

import java.util.List;

public interface TeacherFeatureMapper {

    int deletedByTeacherId(String teacherId);

    int insertBatch(List<TeacherFeature> record);

    List<TeacherFeature> selectByTeacherId(String teacherId);

}