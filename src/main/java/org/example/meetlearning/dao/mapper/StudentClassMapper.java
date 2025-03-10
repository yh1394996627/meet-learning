package org.example.meetlearning.dao.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.StudentClass;

public interface StudentClassMapper {

    int insertEntity(StudentClass record);

    StudentClass selectByRecordId(String recordId);

    int updateEntity(StudentClass record);

    Page<StudentClass> selectByParams(Map<String, Object> params, Page<StudentClass> page);

    Long selectCancelByParams(Map<String, Object> params);

    Long selectCompleteByParams(Map<String, Object> params);

}