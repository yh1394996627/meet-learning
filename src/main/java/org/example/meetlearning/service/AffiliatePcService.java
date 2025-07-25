package org.example.meetlearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.zxing.WriterException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.AffiliateConverter;
import org.example.meetlearning.dao.entity.Affiliate;
import org.example.meetlearning.dao.entity.Student;
import org.example.meetlearning.dao.entity.User;
import org.example.meetlearning.dao.entity.UserFinance;
import org.example.meetlearning.enums.LanguageContextEnum;
import org.example.meetlearning.enums.RoleEnum;
import org.example.meetlearning.service.impl.*;
import org.example.meetlearning.util.BigDecimalUtil;
import org.example.meetlearning.vo.affiliate.*;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.RespVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class AffiliatePcService extends BasePcService {

    private final AffiliateService affiliateService;

    private final StudentService studentService;

    private final StudentClassService studentClassService;

    private final UserFinanceService userFinanceService;

    private final UserService userService;

    public PageVo<AffiliateListPageRespVo> affiliatePage(AffiliateQueryVo queryVo) {
        Page<Affiliate> page = affiliateService.findPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        //获取代理商的学生数量
        List<String> recordIds = page.getRecords().stream().map(Affiliate::getRecordId).distinct().toList();
        Map<String, BigDecimal> countStuMap;
        Map<String, BigDecimal> countCourseMap;
        if (!CollectionUtils.isEmpty(recordIds)) {
            Map<String, Object> params = Map.of("affiliateIds", recordIds);
            List<SelectValueVo> selectValueVos = studentService.selectAffCountByParams(params);
            countStuMap = selectValueVos.stream().collect(Collectors.toMap(SelectValueVo::getValue, v -> new BigDecimal(v.getLabel())));
            //课程数量
            List<SelectValueVo> courseSelectList = studentClassService.selectAffCountByParams(params);
            countCourseMap = courseSelectList.stream().collect(Collectors.toMap(SelectValueVo::getValue, v -> new BigDecimal(v.getLabel())));
        } else {
            countCourseMap = new HashMap<>();
            countStuMap = new HashMap<>();
        }
        PageVo<AffiliateListPageRespVo> pageVo = PageVo.map(page, list -> {
            AffiliateListPageRespVo respVo = AffiliateConverter.INSTANCE.toListResp(list);
            if (countStuMap.containsKey(list.getRecordId())) {
                respVo.setStudentTotal(countStuMap.get(list.getRecordId()));
            }
            if (countCourseMap.containsKey(list.getRecordId())) {
                respVo.setCourseTotal(countCourseMap.get(list.getRecordId()));
            }
            UserFinance userFinance = userFinanceService.selectByUserId(list.getRecordId());
            if (userFinance != null) {
                respVo.setBalance(BigDecimalUtil.nullOrZero(userFinance.getBalanceQty()));
            }
            return respVo;
        });
        return pageVo;
    }

    public RespVo<String> affiliateAdd(String userCode, String userName, AffiliateAddReqVo reqVo) {
        //判断邮箱是否存在
        User user = userService.selectByAccountCode(reqVo.getEmail());
        Assert.isNull(user, getHint(LanguageContextEnum.USER_EXIST) + "【" + reqVo.getEmail() + "】");
        // 验证码校验
        emailVerify(reqVo.getEmail(), reqVo.getVerifyCode());
        // 保存代理商基础信息
        Affiliate affiliate = AffiliateConverter.INSTANCE.toCreateAffiliate(userCode, userName, reqVo);
        affiliateService.insertEntity(affiliate);
        // 创建登陆帐号
        User newUser = addUser(userCode, userName, affiliate.getRecordId(), affiliate.getEmail(), reqVo.getPassword(),
                RoleEnum.AFFILIATE, affiliate.getName(), affiliate.getEnName(), affiliate.getEmail(), null);
        //创建用户关联的课时币
        addFinance(userCode, userName, newUser);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }

    public RespVo<String> affiliateUpdate(String userCode, String userName, AffiliateUpdateReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
        Assert.notNull(affiliate, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        affiliate = AffiliateConverter.INSTANCE.toUpdateAffiliate(userCode, userName, affiliate, reqVo);
        affiliateService.updateEntity(affiliate);
        updateBaseDate(affiliate.getRecordId(), affiliate.getName(), affiliate.getEmail());
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<String> affiliateUpdateRemark(String userCode, String userName, AffiliateRemarkUpdateReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        Affiliate affiliate = affiliateService.findByRecordId(reqVo.getRecordId());
        Assert.notNull(affiliate, getHint(LanguageContextEnum.OBJECT_NOTNULL));
        affiliate.setRemark(reqVo.getRemark());
        affiliate.setUpdator(userCode);
        affiliate.setUpdateName(userName);
        affiliate.setUpdateTime(new Date());
        affiliateService.updateEntity(affiliate);
        return new RespVo<>(getHint(LanguageContextEnum.OPERATION_SUCCESSFUL));
    }


    public RespVo<List<SelectValueVo>> affiliateSelect(RecordIdQueryVo queryVo) {
        List<SelectValueVo> selectValueVos = affiliateService.affiliateSelect();
        if (StringUtils.hasText(queryVo.getRecordId())) {
            selectValueVos = selectValueVos.stream().filter(v -> !v.getValue().equals(queryVo.getRecordId())).toList();
        }
        return new RespVo<>(selectValueVos);
    }


    public RespVo<AffiliateDashboardRespVo> dashboard(String userCode) {
        UserFinance userFinance = userFinanceService.selectByUserId(userCode);
        Affiliate affiliate = affiliateService.findByRecordId(userCode);
        return new RespVo<>(new AffiliateDashboardRespVo(BigDecimalUtil.nullOrZero(affiliate.getCommissionRate()), BigDecimalUtil.nullOrZero(userFinance.getAmount()), BigDecimalUtil.nullOrZero(userFinance.getBalanceQty())));
    }

    public String affiliateQrcode(String recordId) throws IOException, WriterException {
        Affiliate affiliate = affiliateService.findByRecordId(recordId);
        String qrCode = affiliate.getQrCode();
        if (!StringUtils.hasText(qrCode)) {
            qrCode = generateAndUploadQrCode(recordId);
            affiliate.setQrCode(qrCode);
        }
        return downloadFile(qrCode);
    }
}
