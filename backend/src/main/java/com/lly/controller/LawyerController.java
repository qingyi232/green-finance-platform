package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Lawyer;
import com.lly.mapper.LawyerMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/lawyer")
public class LawyerController {
    @Autowired private LawyerMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) Long orgId,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<Lawyer> w = new QueryWrapper<>();
        w.eq("status", 1);
        if (orgId != null) w.eq("org_id", orgId);
        if (keyword != null && !keyword.isEmpty()) w.like("name", keyword);
        w.orderByDesc("rating");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody Lawyer l) {
        l.setCreatedTime(new Date());
        if (l.getStatus() == null) l.setStatus(1);
        mapper.insert(l);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody Lawyer l) { mapper.updateById(l); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
