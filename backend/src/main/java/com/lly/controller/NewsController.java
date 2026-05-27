package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.News;
import com.lly.mapper.NewsMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Autowired private NewsMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<News> w = new QueryWrapper<>();
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (keyword != null && !keyword.isEmpty()) w.like("title", keyword);
        w.orderByDesc("is_top").orderByDesc("published_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/hot")
    public R hot(@RequestParam(defaultValue = "5") Integer limit) {
        Page<News> p = new Page<>(1, limit);
        QueryWrapper<News> w = new QueryWrapper<>();
        w.orderByDesc("is_top").orderByDesc("view_count").orderByDesc("published_time");
        return R.ok().put("data", mapper.selectPage(p, w).getRecords());
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        News n = mapper.selectById(id);
        if (n != null) {
            n.setViewCount((n.getViewCount() == null ? 0 : n.getViewCount()) + 1);
            mapper.updateById(n);
        }
        return R.ok().put("data", n);
    }

    @PostMapping("/save")
    public R save(@RequestBody News n) {
        n.setCreatedTime(new Date());
        if (n.getPublishedTime() == null) n.setPublishedTime(new Date());
        if (n.getViewCount() == null) n.setViewCount(0);
        if (n.getIsTop() == null) n.setIsTop(0);
        mapper.insert(n);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody News n) { mapper.updateById(n); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) {
        for (Long id : ids) mapper.deleteById(id);
        return R.ok();
    }
}
