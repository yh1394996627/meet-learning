package org.example.meetlearning.converter.student;

import javax.annotation.processing.Generated;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.vo.student.StudentListRespVo;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-02-02T22:35:41+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.13 (Oracle Corporation)"
)
public class StudentConverterImpl implements StudentConverter {

    @Override
    public StudentListRespVo toStudentListRespVo(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentListRespVo studentListRespVo = new StudentListRespVo();

        return studentListRespVo;
    }
}
