package org.example.meetlearning.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TextbookConverter;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.service.impl.TextbookService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.textbook.TextbookQueryVo;
import org.example.meetlearning.vo.textbook.TextbookReqVo;
import org.example.meetlearning.vo.textbook.TextbookRespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class TextbookPcService {

    private final TextbookService textbookService;

    public PageVo<TextbookRespVo> textbookPage(TextbookQueryVo queryVo) {
        Page<Textbook> page = textbookService.selectPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        return PageVo.map(page, TextbookConverter.INSTANCE::toRespVo);
    }

    public void add(String userCode, String userName, TextbookReqVo reqVo) {
        Textbook textbook = TextbookConverter.INSTANCE.toCreate(userCode, userName, reqVo);
        textbookService.insertEntity(textbook);
    }

    public void update(String userCode, String userName, TextbookReqVo reqVo) {
        Assert.notNull(reqVo.getRecordId(), "recordId is null");
        Textbook textbook = textbookService.selectByRecordId(reqVo.getRecordId());
        TextbookConverter.INSTANCE.toUpdate(userCode, userName, textbook, reqVo);
        textbookService.updateEntity(textbook);
    }

    public void deleted(RecordIdQueryVo queryVo) {
        Assert.notNull(queryVo.getRecordId(), "recordId is null");
        textbookService.deletedByRecordId(queryVo.getRecordId());
    }

    public List<SelectValueVo> selectValueVos() {
        List<Textbook> list = textbookService.selectByParams(new HashMap<>());
        return list.stream().map(item -> new SelectValueVo(item.getRecordId(), item.getName())).toList();
    }
}
