package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.EvaluationOrg;
import com.lly.mapper.EvaluationOrgMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/evaluation-org")
public class EvaluationOrgController {
    @Autowired private EvaluationOrgMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "20") Integer limit,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<EvaluationOrg> w = new QueryWrapper<>();
        w.eq("status", 1);
        if (keyword != null && !keyword.isEmpty()) w.like("org_name", keyword);
        w.orderByDesc("rating");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody EvaluationOrg e) {
        e.setCreatedTime(new Date());
        if (e.getStatus() == null) e.setStatus(1);
        mapper.insert(e);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody EvaluationOrg e) { mapper.updateById(e); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
