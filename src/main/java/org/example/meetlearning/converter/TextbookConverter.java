package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.dao.entity.TextbookRecord;
import org.example.meetlearning.vo.textbook.TextbookRecordReqVo;
import org.example.meetlearning.vo.textbook.TextbookReqVo;
import org.example.meetlearning.vo.textbook.TextbookRespVo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Date;


@Mapper
public interface TextbookConverter {

    TextbookConverter INSTANCE = Mappers.getMapper(TextbookConverter.class);

    default TextbookRespVo toRespVo(Textbook textbook) {
        TextbookRespVo respVo = new TextbookRespVo();
        respVo.setRecordId(textbook.getRecordId());
        respVo.setName(textbook.getName());
        respVo.setLevelBegin(textbook.getLevelBegin());
        respVo.setLevelEnd(textbook.getLevelEnd());
        respVo.setType(textbook.getType());
        return respVo;
    }

    default Textbook toCreate(String userCode, String userName, TextbookReqVo reqVo) {
        Textbook textbook = new Textbook();
        textbook.setDeleted(false);
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
        record.setTextbookName(textbook.getName());
        record.setName(reqVo.getName());
        record.setCatalog(reqVo.getCatalog());
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
