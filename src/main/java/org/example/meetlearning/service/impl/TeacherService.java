package org.example.meetlearning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.Teacher;
import org.example.meetlearning.dao.mapper.TeacherMapper;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TeacherService {

    private final TeacherMapper teacherMapper;


    /**
     * 根据业务ID查询老师
     *
     * @return 老师信息
     */
    public Teacher selectByRecordId(String recordId) {
        return teacherMapper.selectByRecordId(recordId);
    }


    /**
     * 根据ID查询老师
     *
     * @return 老师信息
     */
    public Teacher selectById(Long id) {
        return teacherMapper.selectById(id);
    }

    public List<BigDecimal> priceGroupList() {
        return teacherMapper.priceGroupList();
    }


    /**
     * 新增老师
     *
     * @param record 老师信息
     */
    public void insertEntity(Teacher record) {
        teacherMapper.insertEntity(record);
    }

    /**
     * 更新老师信息
     *
     * @param record 老师信息
     */
    public void updateEntity(Teacher record) {
        teacherMapper.updateEntity(record);
    }

    /**
     * 分页查询老师
     *
     * @param params 查询参数
     * @param page   分页参数
     * @return 老师信息
     */
    public Page<Teacher> selectPageParams(Map<String, Object> params, Page<Teacher> page) {
        return teacherMapper.selectPageByParams(params, page);
    }

    public List<Teacher> selectListParams(Map<String, Object> params) {
        return teacherMapper.selectListByParams(params);
    }

    /**
     * 分组查管理者
     */
    public List<SelectValueVo> selectGroupManager(Map<String, Object> params) {
        return teacherMapper.selectGroupManager(params);
    }
    /**
     * 分组查管理者
     */
    public List<SelectValueVo> selectAllGroupManager(Map<String, Object> params) {
        return teacherMapper.selectAllGroupManager(params);
    }

    /**
     * 分组查国家
     */
    public List<SelectValueVo> selectGroupCountry(Map<String, Object> params) {
        return teacherMapper.selectGroupCountry(params);
    }

    /**
     * 老师工资统计
     */
    public BigDecimal selectSalaryTotal(Map<String, Object> params) {
        return teacherMapper.selectSalaryTotal(params);
    }

    /**
     * 查询所有老师
     */
    public List<Teacher> selectListByAll() {
        return teacherMapper.selectListByAll();
    }

    /**
     * 查询最热门老师
     */
    public List<Teacher> selectTop5ByQty() {
        return teacherMapper.selectTop5ByQty();
    }

    /**
     * 查询最热门老师
     */
    public List<Teacher> selectTop5ByRating() {
        return teacherMapper.selectTop5ByRating();
    }
}
