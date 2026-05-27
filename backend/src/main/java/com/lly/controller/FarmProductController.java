package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.FarmProduct;
import com.lly.mapper.FarmProductMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/farm-product")
public class FarmProductController {
    @Autowired private FarmProductMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<FarmProduct> w = new QueryWrapper<>();
        w.eq("status", 1);
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (keyword != null && !keyword.isEmpty()) w.like("name", keyword);
        w.orderByDesc("sales");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody FarmProduct p) {
        p.setCreatedTime(new Date());
        if (p.getStatus() == null) p.setStatus(1);
        if (p.getStock() == null) p.setStock(0);
        if (p.getSales() == null) p.setSales(0);
        mapper.insert(p);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody FarmProduct p) { mapper.updateById(p); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
