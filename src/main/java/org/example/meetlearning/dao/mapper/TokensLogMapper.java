package org.example.meetlearning.dao.mapper;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.example.meetlearning.dao.entity.TokensLog;

public interface TokensLogMapper {

    int insertEntity(TokensLog record);

    List<TokensLog> selectByUserId(String userId);

    Page<TokensLog> selectPageByParams(@Param("params") Map<String, Object> params, Page<TokensLog> page);

    int updateEntity(TokensLog record);

}