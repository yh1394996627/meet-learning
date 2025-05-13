package org.example.meetlearning.service;

import com.aliyuncs.utils.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.meetlearning.converter.TextbookConverter;
import org.example.meetlearning.dao.entity.TeacherFeature;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.dao.entity.TextbookRecord;
import org.example.meetlearning.service.impl.TeacherFeatureService;
import org.example.meetlearning.service.impl.TextbookService;
import org.example.meetlearning.vo.common.PageVo;
import org.example.meetlearning.vo.common.RecordIdQueryVo;
import org.example.meetlearning.vo.common.SelectValueVo;
import org.example.meetlearning.vo.textbook.TextbookQueryVo;
import org.example.meetlearning.vo.textbook.TextbookRecordReqVo;
import org.example.meetlearning.vo.textbook.TextbookReqVo;
import org.example.meetlearning.vo.textbook.TextbookRespVo;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class TextbookPcService {

    private final TextbookService textbookService;

    private final TeacherFeatureService teacherFeatureService;

    public PageVo<TextbookRespVo> textbookPage(TextbookQueryVo queryVo) {
        Page<Textbook> page = textbookService.selectPageByParams(queryVo.getParams(), queryVo.getPageRequest());
        List<String> textBookIds = page.getRecords().stream().map(Textbook::getRecordId).toList();
        Map<String, List<TextbookRecord>> textbookRecordMap;
        if (!CollectionUtils.isEmpty(textBookIds)) {
            List<TextbookRecord> records = textbookService.selectByTextbookIds(textBookIds);
            textbookRecordMap = records.stream().collect(Collectors.groupingBy(TextbookRecord::getTextbookId));
        } else {
            textbookRecordMap = new HashMap<>();
        }
        return PageVo.map(page, list -> TextbookConverter.INSTANCE.toRespVo(list, textbookRecordMap));
    }


    public List<TextbookRecordReqVo> fileSearch(RecordIdQueryVo queryVo) {
        Map<String, List<TextbookRecord>> textbookRecordMap;
        List<TextbookRecord> records = textbookService.selectByTextbookIds(List.of(queryVo.getRecordId()));
        return records.stream().map(item -> {
            TextbookRecordReqVo reqVo = new TextbookRecordReqVo();
            reqVo.setRecordId(item.getRecordId());
            reqVo.setName(item.getName());
            reqVo.setCatalog(item.getCatalog());
            reqVo.setPath(item.getCatalog());
            return reqVo;
        }).toList();
    }

    public void add(String userCode, String userName, TextbookReqVo reqVo) {
        Assert.isTrue(!CollectionUtils.isEmpty(reqVo.getCatalogs()), "catalogs cannot be empty");
        Textbook textbook = TextbookConverter.INSTANCE.toCreate(userCode, userName, reqVo);
        List<TextbookRecord> records = reqVo.getCatalogs().stream().map(item -> TextbookConverter.INSTANCE.toCreateRecord(userCode, userName, textbook, item)).toList();
        textbookService.insertEntity(textbook);
        textbookService.insertBatch(records);
    }

    public void update(String userCode, String userName, TextbookReqVo reqVo) {
        Assert.notNull(reqVo.getRecordId(), "recordId is null");
        textbookService.deleteByTextbookId(reqVo.getRecordId());
        Textbook textbook = textbookService.selectByRecordId(reqVo.getRecordId());
        textbookService.updateEntity(textbook);
        TextbookConverter.INSTANCE.toUpdate(userCode, userName, textbook, reqVo);
        textbookService.updateEntity(textbook);
        List<TextbookRecord> records = reqVo.getCatalogs().stream().map(item -> TextbookConverter.INSTANCE.toCreateRecord(userCode, userName, textbook, item)).toList();
        textbookService.insertBatch(records);
    }

    public void deleted(RecordIdQueryVo queryVo) {
        Assert.notNull(queryVo.getRecordId(), "recordId is null");
        textbookService.deletedByRecordId(queryVo.getRecordId());
        textbookService.deleteByTextbookId(queryVo.getRecordId());
    }

    public List<SelectValueVo> selectValueVos(RecordIdQueryVo queryVo) {
        if (StringUtils.isEmpty(queryVo.getRecordId())) {
            List<Textbook> list = textbookService.selectByParams(new HashMap<>());
            return list.stream().map(item -> new SelectValueVo(item.getRecordId(), item.getName())).toList();
        }
        List<TeacherFeature> teacherFeatures = teacherFeatureService.selectByTeacherId(queryVo.getRecordId());
        return teacherFeatures.stream().map(item -> new SelectValueVo(item.getTextbookId(), item.getTextbookName())).toList();
    }
}
