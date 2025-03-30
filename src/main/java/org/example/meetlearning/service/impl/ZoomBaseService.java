package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.ZoomAccountSet;
import org.example.meetlearning.dao.mapper.ZoomAccountSetMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class ZoomBaseService {

    private final ZoomAccountSetMapper zoomAccountSetMapper;

    public List<ZoomAccountSet> selectByParams(Map<String, Object> params) {
        return zoomAccountSetMapper.selectByParams(params);
    }

    public ZoomAccountSet selectByRecordId(String recordId) {
        return zoomAccountSetMapper.selectByRecordId(recordId);
    }
    public ZoomAccountSet selectByAccountId(String accountId) {
        return zoomAccountSetMapper.selectByAccountId(accountId);
    }

    public void insert(ZoomAccountSet record) {
        zoomAccountSetMapper.insert(record);
    }

    public void update(ZoomAccountSet record) {
        zoomAccountSetMapper.updateEntity(record);
    }

    public void deletedByRecordId(String recordId) {
        zoomAccountSetMapper.deletedByRecordId(recordId);
    }
}
