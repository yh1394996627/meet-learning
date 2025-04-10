package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.TextbookRecord;

import java.util.List;

public interface TextbookRecordMapper {

    int deleteByTextbookId(String textbookId);

    int insertBatch(List<TextbookRecord> records);

    List<TextbookRecord> selectByTextbookId(String recordId);

    int updateEntity(TextbookRecord record);

}