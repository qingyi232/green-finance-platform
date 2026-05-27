package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Policy;
import com.lly.mapper.PolicyMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/policy")
public class PolicyController {
    @Autowired private PolicyMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String level,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<Policy> w = new QueryWrapper<>();
        if (level != null && !level.isEmpty()) w.eq("level", level);
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (keyword != null && !keyword.isEmpty()) w.like("title", keyword);
        w.orderByDesc("issue_date");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Policy p = mapper.selectById(id);
        if (p != null) { p.setViewCount((p.getViewCount() == null ? 0 : p.getViewCount()) + 1); mapper.updateById(p); }
        return R.ok().put("data", p);
    }

    @PostMapping("/save")
    public R save(@RequestBody Policy p) {
        p.setCreatedTime(new Date());
        if (p.getViewCount() == null) p.setViewCount(0);
        mapper.insert(p);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody Policy p) { mapper.updateById(p); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
