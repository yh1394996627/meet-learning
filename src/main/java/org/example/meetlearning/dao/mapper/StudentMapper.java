package org.example.meetlearning.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.vo.common.SelectValueVo;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {

    int insertSelective(Student record);

    Student selectByRecordId(String recordId);

    Student selectById(Long id);

    List<Student> selectByParams(@Param("params")Map<String, Object> params);

    List<SelectValueVo> selectAffCountByParams(@Param("params")Map<String, Object> params);

    Page<Student> selectPageByParams(@Param("params")Map<String, Object> params, Page<Student> page);

    int updateByRecordId(Student record);

    int deletedByRecordId(String recordId);

}