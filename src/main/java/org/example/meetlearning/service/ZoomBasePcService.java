package org.example.meetlearning.service;


import cn.hutool.core.util.BooleanUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.meetlearning.converter.ZoomBaseConverter;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.service.impl.ZoomBaseService;
import org.example.meetlearning.service.impl.ZoomOAuthService;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.zoom.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class ZoomBasePcService {

    private final ZoomBaseService zoomBaseService;

    private final ZoomOAuthService zoomOAuthService;

    public List<ZoomBaseListRespVo> zoomList(ZoomBaseListQueryVo queryVo) {
        List<ZoomAccountSet> list = zoomBaseService.selectByParams(queryVo.getParams());
        return list.stream().map(ZoomBaseConverter.INSTANCE::toListResp).toList();
    }

    public void zoomAdd(String userCode, String userName, ZoomBaseReqVo reqVo) {
        ZoomAccountSet zoomAccountSet = ZoomBaseConverter.INSTANCE.toCreateZoomAccountSet(userCode, userName, reqVo);
        String accessToken = zoomOAuthService.getValidAccessToken(reqVo.getClientId(), reqVo.getClientSecret(), reqVo.getAccountId());
        zoomAccountSet.setZoomUserId(zoomOAuthService.getZoomUserIdByEmail(reqVo.getEmail(), accessToken));
        zoomBaseService.insert(zoomAccountSet);
    }

    public void zoomUpdate(String userCode, String userName, ZoomBaseReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId cannot be empty");
        ZoomAccountSet zoomAccountSet = zoomBaseService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(zoomAccountSet, "Zoom config cannot be empty");
        ZoomAccountSet newZoomAccountSet = ZoomBaseConverter.INSTANCE.toUpdateZoomAccountSet(userCode, userName, zoomAccountSet.getId(), reqVo);
        zoomBaseService.update(newZoomAccountSet);
    }

    public void zoomDeleted(RecordIdQueryVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), "recordId cannot be empty");
        zoomBaseService.deletedByRecordId(reqVo.getRecordId());
    }

    public ZoomBaseVerifyRespVo zoomVerify(ZoomBaseStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getAccountId()), "accountId cannot be empty");
            Assert.isTrue(StringUtils.hasText(reqVo.getClientId()), "clientId cannot be empty");
            Assert.isTrue(StringUtils.hasText(reqVo.getClientSecret()), "clientSecret cannot be empty");
            String accessToken = zoomOAuthService.getValidAccessToken(reqVo.getClientId(), reqVo.getClientSecret(), reqVo.getAccountId());
            ZoomBaseVerifyRespVo respVo = zoomOAuthService.isTokenValid(accessToken);
            String zoomUserId = zoomOAuthService.getZoomUserIdByEmail(reqVo.getEmail(), accessToken);
            String verify = zoomOAuthService.getAccountPlanType(reqVo.getAccountId(), accessToken);
            if (BooleanUtil.isTrue(respVo.getStatus()) && StringUtils.hasText(reqVo.getRecordId())) {
                Assert.isTrue(StringUtils.hasText(reqVo.getEmail()), "email cannot be empty");
                ZoomAccountSet zoomAccountSet = zoomBaseService.selectByRecordId(reqVo.getRecordId());
                Assert.notNull(zoomAccountSet, "Zoom config cannot be empty");
                zoomAccountSet.setZoomType(Integer.valueOf(verify));
                zoomAccountSet.setZoomUserId(zoomUserId);
                zoomAccountSet.setIsException(false);
                zoomAccountSet.setZoomStatusMsg(null);
                zoomBaseService.update(zoomAccountSet);
            }
            respVo.setUserZoomId(zoomUserId);
            return respVo;
        } catch (Exception ex) {
            log.error("Failed to verify zoom", ex);
            return new ZoomBaseVerifyRespVo(false, 500, ex.getMessage(), null);
        }
    }

}
