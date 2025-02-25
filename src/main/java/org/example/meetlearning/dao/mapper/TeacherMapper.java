package org.example.meetlearning.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.vo.common.SelectValueVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TeacherMapper {

    int insertEntity(Teacher record);

    Teacher selectByRecordId(String recordId);

    Page<Teacher> selectPageByParams(Map<String, Object> params, Page<Teacher> page);

    /**
     * 查询条件-管理者查询
     */
    List<SelectValueVo> selectGroupManager(Map<String, Object> params);

    /**
     * 薪资统计查询
     */
    BigDecimal selectSalaryTotal(Map<String, Object> params);

    /**
     * 查询条件-国家查询
     */
    List<SelectValueVo> selectGroupCountry(Map<String, Object> params);


    int updateEntity(Teacher record);

}