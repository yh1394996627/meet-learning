package org.example.meetlearning.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.StudentConverter;
import org.example.meetlearning.converter.UserFinanceConverter;
import org.example.meetlearning.dao.entity.*;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.student.*;
import org.example.meetlearning.vo.user.UserStudentFinanceRecordQueryVo;
import org.example.meetlearning.vo.user.UserStudentPayInfoVo;
import org.example.meetlearning.vo.user.UserStudentPayRecordRespVo;
import org.example.meetlearning.vo.user.UserPayReqVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class StudentPcService extends BasePcService {

    private final StudentService studentService;

    private final UserService userService;

    private final UserFinanceService userFinanceService;

    private final UserFinanceRecordService userFinanceRecordService;

    private final BaseConfigService baseConfigService;
    private final TokensLogService tokensLogService;

    /**
     * 学生信息分页查询
     *
     * @param queryVo
     * @return
     */
    public RespVo<PageVo<StudentListRespVo>> studentPage(StudentRequestQueryVo queryVo) {
        Page<Student> page = studentService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        List<String> userIds = page.getRecords().stream().map(Student::getRecordId).toList();
        Map<String, UserFinance> userFinanceMap;
        Map<String, UserFinanceRecord> userFinanceRecordHashMap;
        if (!CollectionUtils.isEmpty(userIds)) {
            //获取学生课时币信息
            List<UserFinance> userFinances = userFinanceService.selectByUserIds(userIds);
            userFinanceMap = userFinances.stream().collect(Collectors.toMap(UserFinance::getUserId, Function.identity()));

            List<UserFinanceRecord> userFinanceRecordList = userFinanceRecordService.selectDateGroupByUserIds(userIds);
            userFinanceRecordHashMap = userFinanceRecordList.stream().collect(Collectors.toMap(UserFinanceRecord::getUserId, Function.identity()));
        } else {
            userFinanceRecordHashMap = new HashMap<>();
            userFinanceMap = new HashMap<>();
        }

        //组装返回数据
        PageVo<StudentListRespVo> pageVo = PageVo.map(page, list -> {
            StudentListRespVo respVo = StudentConverter.INSTANCE.toStudentListRespVo(list);
            if (userFinanceMap.containsKey(list.getRecordId())) {
                UserFinance userFinance = userFinanceMap.get(list.getRecordId());
                respVo.setBalance(BigDecimalUtil.nullOrZero(userFinance.getBalanceQty()));
                respVo.setIsDeleted(BigDecimalUtil.eqZero(respVo.getBalance()));
            }
            if (userFinanceRecordHashMap.containsKey(list.getRecordId())) {
                UserFinanceRecord userFinanceRecord = userFinanceRecordHashMap.get(list.getRecordId());
                respVo.setExpirationTime(userFinanceRecord.getExpirationTime());
            }
            return respVo;
        });
        return new RespVo<>(pageVo);
    }

    @Transactional(rollbackFor = Exception.class)
    public RespVo<String> studentAdd(String userCode, String userName, StudentAddReqVo reqVo) {
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
    }

    public RespVo<String> studentUpdate(String userCode, String userName, StudentUpdateReqVo reqVo) {
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
        Student student = studentService.findByRecordId(recordId);
        student = StudentConverter.INSTANCE.toUpdateStudent(student, reqVo);
        if (StringUtils.hasText(reqVo.getAffiliateId())) {
            User affiliateUser = userService.selectByRecordId(reqVo.getAffiliateId());
            Assert.notNull(affiliateUser, "Affiliate user cannot be empty");
            student.setAffiliateId(affiliateUser.getRecordId());
            student.setAffiliateName(affiliateUser.getName());
        }
        student.setUpdator(userCode);
        student.setUpdateName(userName);
        student.setUpdateTime(new Date());
        studentService.update(student);
        return new RespVo<>("Student update successful");
    }

    public RespVo<String> deleteStudent(StudentRecordReqVo reqVo) {
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
        studentService.deletedByRecordId(recordId);
        //删除登陆帐号和余额信息
        deleteUser(recordId);
        return new RespVo<>("Student deleted successfully");
    }

    public RespVo<String> studentRemarkUpdate(StudentRemarkUpdateReqVo reqVo) {
        String recordId = reqVo.getRecordId();
        Assert.isTrue(StringUtils.hasText(recordId), "recordId cannot be empty");
        Student student = studentService.findByRecordId(recordId);
        student.setRemark(reqVo.getRemark());
        studentService.update(student);
        return new RespVo<>("Updated notes successfully");
    }


    public RespVo<StudentInfoRespVo> studentInfo(RecordIdQueryVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId cannot be empty");
        Student student = studentService.findByRecordId(reqVo.getRecordId());
        Assert.notNull(student, "Student information not obtained");
        StudentInfoRespVo respVo = StudentConverter.INSTANCE.toStudentInfoRespVo(student);
        UserFinance userFinance = userFinanceService.selectByUserId(student.getRecordId());
        Assert.notNull(userFinance, "Student Finance information not obtained");
        respVo.setBalance(userFinance.getBalanceQty());
        respVo.setExpirationTime(userFinance.getExpirationTime());
        return new RespVo<>(respVo);
    }

}
