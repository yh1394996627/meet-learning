package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.dao.entity.TextbookRecord;
import org.example.meetlearning.vo.textbook.TextbookRecordReqVo;
import org.example.meetlearning.vo.textbook.TextbookReqVo;
import org.example.meetlearning.vo.textbook.TextbookRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Mapper
public interface TextbookConverter {

    TextbookConverter INSTANCE = Mappers.getMapper(TextbookConverter.class);

    default TextbookRespVo toRespVo(Textbook textbook, Map<String, List<TextbookRecord>> textbookRecordMap) {
        TextbookRespVo respVo = new TextbookRespVo();
        respVo.setRecordId(textbook.getRecordId());
        respVo.setName(textbook.getName());
        respVo.setLevelBegin(textbook.getLevelBegin());
        respVo.setLevelEnd(textbook.getLevelEnd());
        respVo.setType(textbook.getType());
        if (textbookRecordMap.containsKey(textbook.getRecordId())) {
            respVo.setCatalogs(textbookRecordMap.get(textbook.getRecordId()).stream().map(item -> {
                TextbookRecordReqVo recordRespVo = new TextbookRecordReqVo();
                recordRespVo.setRecordId(item.getRecordId());
                recordRespVo.setName(item.getName());
                recordRespVo.setCatalog(item.getCatalog());
                return recordRespVo;
            }).toList());
        }
        return respVo;
    }

    default Textbook toCreate(String userCode, String userName, TextbookReqVo reqVo) {
        Textbook textbook = new Textbook();
        textbook.setDeleted(false);
        textbook.setRecordId(UUID.randomUUID().toString());
        textbook.setCreator(userCode);
        textbook.setCreateName(userName);
        textbook.setName(reqVo.getName());
        textbook.setLevelBegin(reqVo.getLevelBegin());
        textbook.setLevelEnd(reqVo.getLevelEnd());
        textbook.setType(reqVo.getType());
        return textbook;
    }


    default TextbookRecord toCreateRecord(String userCode, String userName, Textbook textbook, TextbookRecordReqVo reqVo) {
        TextbookRecord record = new TextbookRecord();
        record.setCreator(userCode);
        record.setCreateName(userName);
        record.setCreateTime(new Date());
        record.setTextbookId(textbook.getRecordId());
        record.setRecordId(UUID.randomUUID().toString());
        record.setTextbookName(textbook.getName());
        record.setName(reqVo.getName());
        record.setCatalog(reqVo.getPath());
        if (StringUtils.hasText(reqVo.getCatalog())) {
            record.setCatalog(reqVo.getCatalog());
        }
        return record;
    }

    default void toUpdate(String userCode, String userName, Textbook textbook, TextbookReqVo reqVo) {
        textbook.setName(reqVo.getName());
        textbook.setLevelBegin(reqVo.getLevelBegin());
        textbook.setLevelEnd(reqVo.getLevelEnd());
        textbook.setType(reqVo.getType());
        textbook.setName(reqVo.getName());
        textbook.setUpdator(userCode);
        textbook.setUpdateName(userName);
        textbook.setUpdateTime(new Date());
    }

}
