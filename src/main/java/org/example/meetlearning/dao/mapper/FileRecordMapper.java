package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.FileRecord;

import java.util.List;

public interface FileRecordMapper {

    int deleteByRecordId(String recordId);

    int insertBatch(List<FileRecord> records);

    List<FileRecord> selectByUserId(String userId);
}