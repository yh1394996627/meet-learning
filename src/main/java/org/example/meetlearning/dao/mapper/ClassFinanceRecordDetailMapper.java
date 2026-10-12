package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.ClassFinanceRecordDetail;

import java.util.List;

public interface ClassFinanceRecordDetailMapper {

    /**
     * 批量插入课程扣减明细
     */
    int insertBatch(List<ClassFinanceRecordDetail> list);

    /**
     * 根据课程 record_id 查询有效扣减明细
     */
    List<ClassFinanceRecordDetail> selectByClassId(String classId);

    /**
     * 根据课程 record_id 逻辑删除明细（删除课程回退后标记）
     */
    int deletedByClassId(String classId);

}
