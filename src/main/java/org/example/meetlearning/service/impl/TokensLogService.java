package org.example.meetlearning.service.impl;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.AllArgsConstructor;
import org.example.meetlearning.dao.entity.TokensLog;
import org.example.meetlearning.dao.mapper.TokensLogMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class TokensLogService {

    private final TokensLogMapper tokensLogMapper;

    /**
     * 插入
     */
    public int insertEntity(TokensLog tokensLog) {
        return tokensLogMapper.insertEntity(tokensLog);
    }

    /**
     * 更新
     */
    public int updateEntity(TokensLog tokensLog) {
        return tokensLogMapper.updateEntity(tokensLog);
    }

    /**
     * 根据userId查询列表 不分页
     */
    public List<TokensLog> selectByUserId(String userId) {
        return tokensLogMapper.selectByUserId(userId);
    }


    /**
     * 根据userId查询列表 分页
     */
    public Page<TokensLog> selectPageByUserId(String userId, Page<TokensLog> page) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        return tokensLogMapper.selectPageByParams(params, page);
    }


    /**
     * 分页查询
     */
    public Page<TokensLog> selectPageByParams(Map<String, Object> params, Page<TokensLog> page) {
        return tokensLogMapper.selectPageByParams(params, page);
    }

}
