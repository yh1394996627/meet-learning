package org.example.meetlearning.dao.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.StudentClass;
import org.example.meetlearning.vo.classes.StudentClassPriceGroupVo;
import org.example.meetlearning.vo.common.SelectValueVo;

public interface StudentClassMapper {

    int insertEntity(StudentClass record);

    StudentClass selectByRecordId(String recordId);

    StudentClass selectByMeetId(String meetUuId);

    int updateEntity(StudentClass record);

    int updateClassEntityByStudent(String studentName, String studentId);

    int updateClassEntityByTeacher(String teacherName, String teacherId);

    int deletedClass(String recordId);

    Page<StudentClass> selectPageByParams(@Param("params") Map<String, Object> params, Page<StudentClass> page);

    List<StudentClass> selectByParams(@Param("params") Map<String, Object> params);

    List<StudentClass> selectByCourseDate(Date courseTime, String endTime);

    List<StudentClass> selectAbsentByDate(Date courseTime);

    List<StudentClass> selectClassStatusGroupByParams(String teacherId);

    Long selectCancelByParams(@Param("params") Map<String, Object> params);

    Long selectCompleteByParams(@Param("params") Map<String, Object> params);

    List<SelectValueVo> selectAffCountByParams(@Param("params") Map<String, Object> params);

    List<StudentClass> selectByNowStudentId(String studentId, String nowDate);

    List<StudentClassPriceGroupVo> selectByGltDateTeacherId(String teacherId, Date courseDate);

    List<StudentClass> selectByDateTeacherIdTime(String teacherId, Date courseDate, String beginTime, String endTime, String courseType);

    void updateByGltDateTeacherId(String teacherId, Date courseDate);

    List<StudentClass> selectClassByTimeBt(Date courseDate, String beginTime);

    /**
     * 查询学生在指定日期和时间段是否有预约（排除已取消的）
     */
    Long countByStudentIdDateTime(String studentId, Date courseDate, String beginTime, String endTime);


}