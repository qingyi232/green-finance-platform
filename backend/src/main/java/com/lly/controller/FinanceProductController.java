package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.FinanceProduct;
import com.lly.mapper.FinanceProductMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/finance-product")
public class FinanceProductController {
    @Autowired private FinanceProductMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<FinanceProduct> w = new QueryWrapper<>();
        w.eq("status", 1);
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (keyword != null && !keyword.isEmpty()) w.like("name", keyword);
        w.orderByDesc("apply_count");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/match")
    public R match(@RequestBody Map<String, Object> body) {
        BigDecimal amount = body.get("amount") == null ? null : new BigDecimal(String.valueOf(body.get("amount")));
        Integer term = body.get("term") == null ? null : Integer.valueOf(String.valueOf(body.get("term")));
        String role = (String) body.get("role");

        List<FinanceProduct> all = mapper.selectList(new QueryWrapper<FinanceProduct>().eq("status", 1));
        all.removeIf(p -> {
            if (amount != null) {
                if (p.getLoanMin() != null && amount.compareTo(p.getLoanMin()) < 0) return true;
                if (p.getLoanMax() != null && amount.compareTo(p.getLoanMax()) > 0) return true;
            }
            if (term != null) {
                if (p.getTermMin() != null && term < p.getTermMin()) return true;
                if (p.getTermMax() != null && term > p.getTermMax()) return true;
            }
            if (role != null && !role.isEmpty() && p.getTargetUsers() != null) {
                return !p.getTargetUsers().contains(role);
            }
            return false;
        });
        return R.ok().put("data", all);
    }

    @PostMapping("/save")
    public R save(@RequestBody FinanceProduct p) {
        p.setCreatedTime(new Date());
        if (p.getStatus() == null) p.setStatus(1);
        if (p.getApplyCount() == null) p.setApplyCount(0);
        mapper.insert(p);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody FinanceProduct p) { mapper.updateById(p); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
