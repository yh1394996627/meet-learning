package org.example.meetlearning.dao.mapper;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.vo.common.SelectValueVo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TeacherMapper {

    int insertEntity(Teacher record);

    Teacher selectByRecordId(String recordId);

    Teacher selectById(Long id);

    List<BigDecimal> priceGroupList();

    Page<Teacher> selectPageByParams(@Param("params") Map<String, Object> params, Page<Teacher> page);

    List<Teacher> selectListByParams(@Param("params") Map<String, Object> params);

    List<Teacher> selectListByAll();

    /**
     * 查询条件-管理者查询
     */
    List<SelectValueVo> selectGroupManager(@Param("params") Map<String, Object> params);
    /**
     * 查询条件-管理者查询
     */
    List<SelectValueVo> selectAllGroupManager(@Param("params") Map<String, Object> params);

    /**
     * 薪资统计查询
     */
    BigDecimal selectSalaryTotal(@Param("params") Map<String, Object> params);

    /**
     * 查询条件-国家查询
     */
    List<SelectValueVo> selectGroupCountry(@Param("params") Map<String, Object> params);


    int updateEntity(Teacher record);

}