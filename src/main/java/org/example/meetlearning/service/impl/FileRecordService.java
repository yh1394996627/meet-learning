package org.example.meetlearning.service.impl;

import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.FileRecord;
import org.example.meetlearning.dao.mapper.FileRecordMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class FileRecordService {

    private final FileRecordMapper fileRecordMapper;


    /**
     * 批量插入
     */
    public int insertBatch(List<FileRecord> records) {
        return fileRecordMapper.insertBatch(records);
    }

    /**
     * 根据recordId删除
     */
    public int deleteByRecordId(String recordId) {
        return fileRecordMapper.deleteByRecordId(recordId);
    }


    /**
     * 根据userId查询
     */
    public List<FileRecord> selectByUserId(String userId) {
        return fileRecordMapper.selectByUserId(userId);
    }


}
