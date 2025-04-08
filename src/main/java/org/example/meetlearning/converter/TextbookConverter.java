package org.example.meetlearning.converter;

import org.example.meetlearning.dao.entity.Textbook;
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
        respVo.setCatalog(textbook.getCatalog());
        return respVo;
    }

    default Textbook toCreate(String userCode, String userName, TextbookReqVo reqVo) {
        Textbook textbook = new Textbook();
        textbook.setCreator(userCode);
        textbook.setCreateName(userName);
        textbook.setName(reqVo.getName());
        textbook.setLevelBegin(reqVo.getLevelBegin());
        textbook.setLevelEnd(reqVo.getLevelEnd());
        textbook.setType(reqVo.getType());
        textbook.setCatalog(reqVo.getCatalog());
        return textbook;
    }

    default Textbook toUpdate(String userCode, String userName, Textbook textbook, TextbookReqVo reqVo) {
        textbook.setName(reqVo.getName());
        textbook.setLevelBegin(reqVo.getLevelBegin());
        textbook.setLevelEnd(reqVo.getLevelEnd());
        textbook.setType(reqVo.getType());
        textbook.setCatalog(reqVo.getCatalog());
        textbook.setUpdator(userCode);
        textbook.setUpdateName(userName);
        textbook.setUpdateTime(new Date());
        return textbook;
    }

}
