package org.example.meetlearning.service;


import cn.hutool.core.util.BooleanUtil;
import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.BooleanUtils;
import org.example.meetlearning.converter.ZoomBaseConverter;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.enums.LanguageContextEnum;
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
public class ZoomBasePcService extends BasePcService {

    private final ZoomBaseService zoomBaseService;

    private final ZoomOAuthService zoomOAuthService;

    public List<ZoomBaseListRespVo> zoomList(ZoomBaseListQueryVo queryVo) {
        List<ZoomAccountSet> list = zoomBaseService.selectByParams(queryVo.getParams());
        return list.stream().map(ZoomBaseConverter.INSTANCE::toListResp).toList();
    }

    public void zoomAdd(String userCode, String userName, ZoomBaseReqVo reqVo) {
        ZoomAccountSet zoomAccountSet = ZoomBaseConverter.INSTANCE.toCreateZoomAccountSet(userCode, userName, reqVo);
        zoomAccountSet.setZoomType(1);
        zoomAccountSet.setZoomStatusMsg("Unverified");
        zoomAccountSet.setIsException(true);
        zoomBaseService.insert(zoomAccountSet);
    }

    public void zoomUpdate(String userCode, String userName, ZoomBaseReqVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        ZoomAccountSet zoomAccountSet = zoomBaseService.selectByRecordId(reqVo.getRecordId());
        Assert.notNull(zoomAccountSet, "Zoom config" + getHint(LanguageContextEnum.OBJ_NOTNULL));
        ZoomAccountSet newZoomAccountSet = ZoomBaseConverter.INSTANCE.toUpdateZoomAccountSet(userCode, userName, zoomAccountSet.getId(), reqVo);
        zoomAccountSet.setZoomType(1);
        zoomAccountSet.setZoomStatusMsg("Unverified");
        zoomAccountSet.setIsException(false);
        zoomBaseService.update(newZoomAccountSet);
    }

    public void zoomDeleted(RecordIdQueryVo reqVo) {
        Assert.isTrue(StringUtils.hasText(reqVo.getRecordId()), getHint(LanguageContextEnum.OBJECT_NOTNULL));
        zoomBaseService.deletedByRecordId(reqVo.getRecordId());
    }

    public ZoomBaseVerifyRespVo zoomVerify(ZoomBaseStatusReqVo reqVo) {
        try {
            Assert.isTrue(StringUtils.hasText(reqVo.getAccountId()), "accountId" + getHint(LanguageContextEnum.OBJ_NOTNULL));
            Assert.isTrue(StringUtils.hasText(reqVo.getClientId()), "clientId" + getHint(LanguageContextEnum.OBJ_NOTNULL));
            Assert.isTrue(StringUtils.hasText(reqVo.getClientSecret()), "clientSecret" + getHint(LanguageContextEnum.OBJ_NOTNULL));
            String accessToken = zoomOAuthService.getValidAccessToken(reqVo.getClientId(), reqVo.getClientSecret(), reqVo.getAccountId());
            ZoomBaseVerifyRespVo respVo = zoomOAuthService.isTokenValid(reqVo.getAccountId(), accessToken);
            JsonObject obj = zoomOAuthService.getUserInfo(reqVo.getEmail());
            String zoomUserId = obj.get("id").getAsString();
            if (BooleanUtil.isTrue(respVo.getStatus()) && StringUtils.hasText(reqVo.getRecordId())) {
                Assert.isTrue(StringUtils.hasText(reqVo.getEmail()), "email" + getHint(LanguageContextEnum.OBJ_NOTNULL));
                ZoomAccountSet zoomAccountSet = zoomBaseService.selectByRecordId(reqVo.getRecordId());
                Assert.notNull(zoomAccountSet, "Zoom config" + getHint(LanguageContextEnum.OBJ_NOTNULL));
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
