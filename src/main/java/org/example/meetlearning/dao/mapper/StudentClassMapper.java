package org.example.meetlearning.dao.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.StudentClass;

public interface StudentClassMapper {

    int insertEntity(StudentClass record);

    StudentClass selectByRecordId(String recordId);

    int updateEntity(StudentClass record);

    Page<StudentClass> selectByParams(@Param("params")Map<String, Object> params, Page<StudentClass> page);

    Long selectCancelByParams(@Param("params")Map<String, Object> params);

    Long selectCompleteByParams(@Param("params")Map<String, Object> params);

}