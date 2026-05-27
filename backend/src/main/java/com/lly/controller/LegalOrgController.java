package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.LegalOrg;
import com.lly.mapper.LegalOrgMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/legal-org")
public class LegalOrgController {
    @Autowired private LegalOrgMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) String orgType,
                  @RequestParam(required = false) String serviceArea,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<LegalOrg> w = new QueryWrapper<>();
        w.eq("status", 1);
        if (orgType != null && !orgType.isEmpty()) w.eq("org_type", orgType);
        if (serviceArea != null && !serviceArea.isEmpty()) w.like("service_area", serviceArea);
        if (keyword != null && !keyword.isEmpty()) w.like("org_name", keyword);
        w.orderByDesc("rating");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody LegalOrg o) {
        o.setCreatedTime(new Date());
        if (o.getStatus() == null) o.setStatus(1);
        mapper.insert(o);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody LegalOrg o) { mapper.updateById(o); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
