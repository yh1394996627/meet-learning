package org.example.meetlearning.service.impl;


import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.dao.mapper.StudentClassMapper;
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
     * 更新
     */
    public int updateEntity(StudentClass record) {
        return studentClassMapper.updateEntity(record);
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
     * 根据recordId查询
     */
    public List<StudentClass> selectByNowStudentId(String studentId) {
        return studentClassMapper.selectByNowStudentId(studentId, DateUtil.format(new Date(), "yyyy-MM-dd"));
    }
}
