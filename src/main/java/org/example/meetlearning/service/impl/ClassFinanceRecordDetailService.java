package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.ClassFinanceRecordDetail;
import org.example.meetlearning.dao.mapper.ClassFinanceRecordDetailMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ClassFinanceRecordDetailService {

    private final ClassFinanceRecordDetailMapper classFinanceRecordDetailMapper;

    /**
     * 批量插入课程扣减明细
     */
    public int insertBatch(List<ClassFinanceRecordDetail> list) {
        return classFinanceRecordDetailMapper.insertBatch(list);
    }

    /**
     * 根据课程 record_id 查询有效扣减明细
     */
    public List<ClassFinanceRecordDetail> selectByClassId(String classId) {
        return classFinanceRecordDetailMapper.selectByClassId(classId);
    }

    /**
     * 根据课程 record_id 逻辑删除明细（删除课程回退后标记）
     */
    public int deletedByClassId(String classId) {
        return classFinanceRecordDetailMapper.deletedByClassId(classId);
    }

}
