package org.example.meetlearning.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.Textbook;
import org.example.meetlearning.dao.mapper.TextbookMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TextbookService {

    private final TextbookMapper textbookMapper;

    /**
     * 新增教材
     */
    public void insertEntity(Textbook textbook) {
        textbookMapper.insert(textbook);
    }

    /**
     * 根据条件查询分页
     */
    public Page<Textbook> selectPageByParams(Map<String, Object> params, Page<Textbook> page) {
        return textbookMapper.selectPageByParams(params, page);
    }

    /**
     * 根据条件查询分页
     */
    public List<Textbook> selectByParams(Map<String, Object> params) {
        return textbookMapper.selectByParams(params);
    }

    /**
     * 根据记录id查询
     */
    public Textbook selectByRecordId(String recordId){
        return textbookMapper.selectByRecordId(recordId);
    }

    /**
     * 更新教材
     */
    public void updateEntity(Textbook textbook) {
        textbookMapper.update(textbook);
    }

    /**
     * 根据记录id删除
     */
    public void deletedByRecordId(String recordId) {
        textbookMapper.deletedByRecordId(recordId);
    }


}
