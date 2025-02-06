package org.example.meetlearning.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    @Autowired
    private StudentMapper studentMapper;

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
     * 保存学生信息
     *
     * @param student 学生信息
     */
    public void save(Student student) {
        studentMapper.insertSelective(student);
    }


    /**
     * 更新学生信息
     *Ï
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
