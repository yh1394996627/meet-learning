package org.example.meetlearning.converter;

import java.util.Date;
import java.util.UUID;

import cn.hutool.core.util.BooleanUtil;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.vo.zoom.ZoomBaseListRespVo;
import org.example.meetlearning.vo.zoom.ZoomBaseReqVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ZoomBaseConverter {

    ZoomBaseConverter INSTANCE = Mappers.getMapper(ZoomBaseConverter.class);

    default ZoomBaseListRespVo toListResp(ZoomAccountSet zoomAccountSet) {
        ZoomBaseListRespVo respVo = new ZoomBaseListRespVo();
        respVo.setRecordId(zoomAccountSet.getRecordId());
        respVo.setEmail(zoomAccountSet.getZoomEmail());
        respVo.setAccountId(zoomAccountSet.getZoomAccountId());
        respVo.setClientId(zoomAccountSet.getZoomClientId());
        respVo.setClientSecret(zoomAccountSet.getZoomClientSecret());
        respVo.setSecretToken(zoomAccountSet.getSecretToken());
        respVo.setVerificationToken(zoomAccountSet.getVerificationToken());
        respVo.setType(zoomAccountSet.getZoomType());
        respVo.setUsedQty(zoomAccountSet.getUsedQty());
        respVo.setTotalUsedQty(99);
        respVo.setStatusMsg(zoomAccountSet.getZoomStatusMsg());
        respVo.setIsException(BooleanUtil.isTrue(zoomAccountSet.getIsException()));
        return respVo;
    }

    default ZoomAccountSet toCreateZoomAccountSet(String userCode, String userName, ZoomBaseReqVo reqVo) {
        ZoomAccountSet zoomAccountSet = new ZoomAccountSet();
        zoomAccountSet.setDeleted(false);
        zoomAccountSet.setCreator(userCode);
        zoomAccountSet.setCreateName(userName);
        zoomAccountSet.setCreateTime(new Date());
        zoomAccountSet.setRecordId(UUID.randomUUID().toString());
        zoomAccountSet.setZoomEmail(reqVo.getEmail());
        zoomAccountSet.setZoomAccountId(reqVo.getAccountId());
        zoomAccountSet.setZoomClientId(reqVo.getClientId());
        zoomAccountSet.setZoomClientSecret(reqVo.getClientSecret());
        zoomAccountSet.setSecretToken(reqVo.getSecretToken());
        zoomAccountSet.setVerificationToken(reqVo.getVerificationToken());
        zoomAccountSet.setTotalUsedQty(0);
        zoomAccountSet.setUsedQty(0);
        zoomAccountSet.setZoomType(0);
        return zoomAccountSet;
    }

    default ZoomAccountSet toUpdateZoomAccountSet(String userCode, String userName, Long id, ZoomBaseReqVo reqVo) {
        ZoomAccountSet newZoomAccountSet = new ZoomAccountSet();
        newZoomAccountSet.setId(id);
        newZoomAccountSet.setDeleted(false);
        newZoomAccountSet.setUpdator(userCode);
        newZoomAccountSet.setUpdateName(userName);
        newZoomAccountSet.setUpdateTime(new Date());
        newZoomAccountSet.setZoomEmail(reqVo.getEmail());
        newZoomAccountSet.setZoomAccountId(reqVo.getAccountId());
        newZoomAccountSet.setZoomClientId(reqVo.getClientId());
        newZoomAccountSet.setZoomClientSecret(reqVo.getClientSecret());
        newZoomAccountSet.setVerificationToken(reqVo.getVerificationToken());
        newZoomAccountSet.setSecretToken(reqVo.getSecretToken());
        return newZoomAccountSet;
    }


}
