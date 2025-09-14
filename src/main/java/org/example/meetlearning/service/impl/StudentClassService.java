package org.example.meetlearning.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.mapper.StudentClassMapper;
import org.example.meetlearning.vo.classes.StudentClassPriceGroupVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StudentClassService {

    private final StudentClassMapper studentClassMapper;

    /**
     * 根据recordId查询
     */
    public StudentClass selectByRecordId(String recordId) {
        return studentClassMapper.selectByRecordId(recordId);
    }

    /**
     * 根据meetUuId查询
     */
    public StudentClass selectByMeetId(String meetUuId) {
        return studentClassMapper.selectByMeetId(meetUuId);
    }

    /**
     * 根据条件查询
     */
    public Page<StudentClass> selectPageByParams(Map<String, Object> params, Page<StudentClass> pageRequest) {
        return studentClassMapper.selectPageByParams(params, pageRequest);
    }

    /**
     * 根据条件查询
     */
    public List<StudentClass> selectByParams(Map<String, Object> params) {
        return studentClassMapper.selectByParams(params);
    }

    /**
     * 获取课程列表
     */
    public List<StudentClass> selectByCourseDate(Date courseDate, String endTime) {
        return studentClassMapper.selectByCourseDate(courseDate, endTime);
    }

    /**
     * 获取缺勤列表
     */
    public List<StudentClass> selectAbsentByDate(Date courseDate) {
        return studentClassMapper.selectAbsentByDate(courseDate);
    }

    /**
     * 根据时间段查询课程
     */
    public List<StudentClass> selectClassByTimeBt(Date courseTime, String beginTime) {
        return studentClassMapper.selectClassByTimeBt(courseTime, beginTime);
    }

    /**
     * 根据老师查询课程状态
     */
    public List<StudentClass> selectClassStatusGroupByParams(String teacherId) {
        return studentClassMapper.selectClassStatusGroupByParams(teacherId);
    }

    /**
     * 更新
     */
    public int updateEntity(StudentClass record) {
        return studentClassMapper.updateEntity(record);
    }

    /**
     * 更新
     */
    public int updateClassEntityByStudent(String studentName,String studentId) {
        return studentClassMapper.updateClassEntityByStudent(studentName,studentId);
    }

    /**
     * 更新
     */
    public int updateClassEntityByTeacher(String teacherName,String studentId) {
        return studentClassMapper.updateClassEntityByTeacher(teacherName,studentId);
    }

    /**
     * 新增
     */
    public int insertEntity(StudentClass record) {
        return studentClassMapper.insertEntity(record);
    }

    /**
     * 查询老师取消总数
     */
    public Long selectCancelByParams(Map<String, Object> params) {
        return studentClassMapper.selectCancelByParams(params);
    }

    /**
     * 查询预约总数
     */
    public Long selectCompleteByParams(Map<String, Object> params) {
        return studentClassMapper.selectCompleteByParams(params);
    }

    /**
     * 根据条件查询
     */
    public List<SelectValueVo> selectAffCountByParams(Map<String, Object> params) {
        return studentClassMapper.selectAffCountByParams(params);
    }


    /**
     * 根据studentId查询
     */
    public List<StudentClass> selectByNowStudentId(String studentId) {
        return studentClassMapper.selectByNowStudentId(studentId, DateUtil.format(new Date(), "yyyy-MM-dd"));
    }

    /**
     * 根据teacherId和courseDate查询未核销的课程
     */
    public List<StudentClassPriceGroupVo> selectByGltDateTeacherId(String teacherId, Date courseDate) {
        return studentClassMapper.selectByGltDateTeacherId(teacherId, courseDate);
    }

    /**
     * 查询是否又课程
     */
    public List<StudentClassPriceGroupVo> selectByDateTeacherIdTime(String teacherId, Date courseDate, String beginTime, String endTime, String courseType) {
        return studentClassMapper.selectByDateTeacherIdTime(teacherId, courseDate, beginTime, endTime, courseType);
    }

     public void updateByGltDateTeacherId(String teacherId, Date courseDate) {
        studentClassMapper.updateByGltDateTeacherId(teacherId, courseDate);
    }



}
