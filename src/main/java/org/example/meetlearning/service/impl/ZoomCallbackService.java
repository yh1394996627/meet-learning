package org.example.meetlearning.service.impl;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.ZoomCallbackMsg;
import org.example.meetlearning.dao.mapper.ZoomCallbackMsgMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ZoomCallbackService {

    private final ZoomCallbackMsgMapper zoomCallbackMsgMapper;

    /**
     * ZOOM回调异常信息写入
     */
    public void insertMsg(String token, String payload) {
        ZoomCallbackMsg zoomCallbackMsg = new ZoomCallbackMsg();
        zoomCallbackMsg.setDeleted(false);
        zoomCallbackMsg.setCreateTime(new Date());
        zoomCallbackMsg.setRecordId(UUID.randomUUID().toString());
        zoomCallbackMsg.setName("");
        zoomCallbackMsg.setToken(token);
        zoomCallbackMsg.setPayload(payload);
        zoomCallbackMsgMapper.insert(zoomCallbackMsg);
    }

    public void deleteByRecordId(String recordId) {
        zoomCallbackMsgMapper.deleteByRecordId(recordId);
    }


    public List<ZoomCallbackMsg> selectByRecordIds(List<String> recordIds) {
        return zoomCallbackMsgMapper.selectByRecordIds(recordIds);
    }

}
