package org.example.meetlearning.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.mapper.StudentMapper;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentMapper studentMapper;

    /**
     * 根据recordId查询学生信息
     *
     * @param recordId 业务ID
     * @return 学生信息
     */
    public Student findByRecordId(String recordId) {
        return studentMapper.selectByRecordId(recordId);
    }

    /**
     * 根据recordIds查询学生信息
     */
    public List<Student> findByRecordIds(List<String> recordIds) {
        if (CollectionUtils.isEmpty(recordIds)) {
            return new ArrayList<>();
        }
        return studentMapper.selectByRecordIds(recordIds);
    }

    /**
     * 根据ID查询学生信息
     *
     * @param id 业务ID
     * @return 学生信息
     */
    public Student findById(Long id) {
        return studentMapper.selectById(id);
    }


    /**
     * 根据Map参数查询学生信息
     *
     * @param params 查询参数
     * @return 学生信息
     */
    public List<Student> findByParams(Map<String, Object> params) {
        return studentMapper.selectByParams(params);
    }

    /**
     * 根据Map参数查询学生信息 分页
     *
     * @param params 查询参数
     * @return 学生信息
     */
    public Page<Student> findPageByParams(Map<String, Object> params, Page<Student> page) {
        return studentMapper.selectPageByParams(params, page);
    }

    /**
     * 查询代理商下的学生数量
     */
    public List<SelectValueVo> selectAffCountByParams(Map<String, Object> params) {
        return studentMapper.selectAffCountByParams(params);
    }


    /**
     * 保存学生信息
     *
     * @param student 学生信息
     */
    public void save(Student student) {
        studentMapper.insertSelective(student);
    }


    /**
     * 更新学生信息
     * Ï
     *
     * @param student 学生信息
     */
    public void update(Student student) {
        studentMapper.updateByRecordId(student);
    }

    /**
     * 删除学生信息
     *
     * @param recordId 学生recordId
     */
    public void deletedByRecordId(String recordId) {
        studentMapper.deletedByRecordId(recordId);
    }


}
