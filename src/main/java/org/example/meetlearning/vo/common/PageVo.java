package org.example.meetlearning.vo.common;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor
public class PageVo<T> extends Page<T> {

    public static <S, T> PageVo<T> map(Page<S> page, List<T> list) {
        PageVo<T> pageVo = new PageVo<T>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setSize(page.getSize());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        pageVo.setRecords(list);
        return pageVo;
    }

    public static <S, T> PageVo<T> map(Page<S> page, Function<S, T> mapper) {
        PageVo<T> pageVo = new PageVo<T>();
        pageVo.setCurrent(page.getCurrent());
        pageVo.setSize(page.getSize());
        pageVo.setTotal(page.getTotal());
        pageVo.setPages(page.getPages());
        pageVo.setRecords(page.getRecords().stream().map(mapper).collect(Collectors.toList()));
        return pageVo;
    }

    public static <T> PageVo<T> getNewPageVo(long current, long size, long total, long pages, List<T> records) {
        PageVo<T> pageVo = new PageVo<>();
        pageVo.setRecords(records);
        pageVo.setTotal(total);
        pageVo.setSize(size);
        pageVo.setCurrent(current);
        pageVo.setPages(pages);
        return pageVo;

    }
}
