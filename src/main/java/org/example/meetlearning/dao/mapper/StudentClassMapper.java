package org.example.meetlearning.dao.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.vo.common.SelectValueVo;

public interface StudentClassMapper {

    int insertEntity(StudentClass record);

    StudentClass selectByRecordId(String recordId);

    StudentClass selectByMeetUuId(String meetUuId);

    int updateEntity(StudentClass record);

    Page<StudentClass> selectByParams(@Param("params")Map<String, Object> params, Page<StudentClass> page);

    Long selectCancelByParams(@Param("params")Map<String, Object> params);

    Long selectCompleteByParams(@Param("params")Map<String, Object> params);

    List<SelectValueVo> selectAffCountByParams(@Param("params")Map<String, Object> params);
}