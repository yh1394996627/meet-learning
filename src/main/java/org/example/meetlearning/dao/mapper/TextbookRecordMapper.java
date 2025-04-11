package org.example.meetlearning.dao.mapper;

import org.example.meetlearning.dao.entity.TextbookRecord;

import java.util.List;

public interface TextbookRecordMapper {

    int deleteByTextbookId(String textbookId);

    int insertBatch(List<TextbookRecord> records);

    List<TextbookRecord> selectByTextbookId(String textbookId);

    List<TextbookRecord> selectByTextbookIds(List<String> textbookIds);

    int updateEntity(TextbookRecord record);

}