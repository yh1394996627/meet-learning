package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.example.meetlearning.converter.student.StudentConverter;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.service.impl.StudentService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.StudentListRespVo;
import org.example.meetlearning.vo.student.StudentRequestQueryVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentPcService {

    @Autowired
    private StudentService studentService;


    /**
     * 学生信息分页查询
     *
     * @param queryVo
     * @return
     */
    public RespVo<PageVo<StudentListRespVo>> studentPage(String userCode, StudentRequestQueryVo queryVo) {
        Page<Student> page = studentService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        PageVo<StudentListRespVo> pageVo = PageVo.map(page, StudentConverter.INSTANCE::toStudentListRespVo);
        return new RespVo<>(pageVo);
    }


}
