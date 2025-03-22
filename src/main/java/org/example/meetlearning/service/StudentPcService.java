package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.StudentConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.StudentService;
import org.example.meetlearning.service.impl.UserFinanceService;
import org.example.meetlearning.service.impl.UserService;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class StudentPcService extends BasePcService {

    private final StudentService studentService;

    private final UserService userService;

    private final UserFinanceService userFinanceService;


    /**
     * 学生信息分页查询
     *
     * @param queryVo
     * @return
     */
    public RespVo<PageVo<StudentListRespVo>> studentPage(StudentRequestQueryVo queryVo) {
        Page<Student> page = studentService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        List<String> userIds = page.getRecords().stream().map(Student::getRecordId).toList();
        //获取学生课时币信息
        List<UserFinance> userFinances = userFinanceService.selectByUserIds(userIds);
        Map<String, UserFinance> userFinanceMap = userFinances.stream().collect(Collectors.toMap(UserFinance::getUserId, Function.identity()));

        //组装返回数据
        PageVo<StudentListRespVo> pageVo = PageVo.map(page, list -> {
            StudentListRespVo respVo = StudentConverter.INSTANCE.toStudentListRespVo(list);
            if (userFinanceMap.containsKey(list.getRecordId())) {
                UserFinance userFinance = userFinanceMap.get(list.getRecordId());
                respVo.setBalance(userFinance.getBalanceQty());
                respVo.setExpirationTime(userFinance.getExpirationTime());
                respVo.setIsDeleted(BigDecimalUtil.eqZero(respVo.getBalance()));
            }
            return respVo;
        });
        return new RespVo<>(pageVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public RespVo<String> studentAdd(String userCode, String userName, StudentAddReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getEnName()), "Email cannot be empty");
            Assert.isTrue(StringUtils.hasText(reqVo.getPassword()), "Password cannot be empty");

            Student student = StudentConverter.INSTANCE.toCreateStudent(userCode, userName, reqVo);
            //如果是代理商 则添加代理商信息
            User managerUser = userService.selectByRecordId(userCode);
            if (managerUser != null && StringUtils.pathEquals(RoleEnum.AFFILIATE.name(), managerUser.getType())) {
                student.setAffiliateId(managerUser.getRecordId());
                student.setAffiliateName(managerUser.getName());
            }
            studentService.save(student);
            //创建登陆帐号
            User newUser = addUser(userCode, userName, student.getRecordId(), student.getEmail(), reqVo.getPassword(),
                    RoleEnum.STUDENT, student.getName(), student.getName(), student.getEmail());

            //创建用户关联的课时币
            addFinance(userCode, userName, newUser);
            return new RespVo<>("New successfully added");
        } catch (Exception ex) {
            log.error("Addition failed", ex);
            return new RespVo<>("Addition failed", false, ex.getMessage());
        }
    }

    public RespVo<String> studentUpdate(String userCode, String userName, StudentUpdateReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
            Student student = studentService.findByRecordId(recordId);
            student = StudentConverter.INSTANCE.toUpdateStudent(student, reqVo);
            if (StringUtils.hasText(reqVo.getAffiliateId())) {
                User affiliateUser = userService.selectByRecordId(userCode);
                Assert.notNull(affiliateUser, "Affiliate user cannot be empty");
                student.setAffiliateId(affiliateUser.getRecordId());
                student.setAffiliateName(affiliateUser.getName());
            }
            student.setUpdator(userCode);
            student.setUpdateName(userName);
            student.setUpdateTime(new Date());
            studentService.update(student);
            return new RespVo<>("Student update successful");
        } catch (Exception ex) {
            log.error("Failed to update students", ex);
            return new RespVo<>("Failed to update students", false, ex.getMessage());
        }
    }

    public RespVo<String> deleteStudent(StudentRecordReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
            studentService.deletedByRecordId(recordId);
            return new RespVo<>("Student deleted successfully");
        } catch (Exception ex) {
            log.error("Failed to delete student", ex);
            return new RespVo<>("Failed to delete student", false, ex.getMessage());
        }
    }

    public RespVo<String> studentRemarkUpdate(StudentRemarkUpdateReqVo reqVo) {
        try {
            String recordId = reqVo.getRecordId();
            Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
            Student student = studentService.findByRecordId(recordId);
            student.setRemark(reqVo.getRemark());
            studentService.update(student);
            return new RespVo<>("Updated notes successfully");
        } catch (Exception ex) {
            log.error("Failed to update notes", ex);
            return new RespVo<>("Failed to update notes", false, ex.getMessage());
        }
    }


    public RespVo<StudentInfoRespVo> studentInfo(RecordIdQueryVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId cannot be empty");
            Student student = studentService.findByRecordId(reqVo.getRecordId());
            Assert.notNull(student, "Student information not obtained");
            StudentInfoRespVo respVo = StudentConverter.INSTANCE.toStudentInfoRespVo(student);
            UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
            Assert.notNull(userFinance, "Student Finance information not obtained");
            respVo.setBalance(userFinance.getBalanceQty());
            respVo.setExpirationTime(userFinance.getExpirationTime());
            return new RespVo<>(respVo);
        } catch (Exception e) {
            log.error("Query failed", e);
            return new RespVo<>(null, false, e.getMessage());
        }
    }
}
