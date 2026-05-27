package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Demand;
import com.lly.mapper.DemandMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/demand")
public class DemandController {
    @Autowired private DemandMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String subCategory,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<Demand> w = new QueryWrapper<>();
        w.eq("status", "征集中").eq("audit_status", "已通过");
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (subCategory != null && !subCategory.isEmpty()) w.eq("sub_category", subCategory);
        if (keyword != null && !keyword.isEmpty()) w.like("title", keyword);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Demand d = mapper.selectById(id);
        if (d != null) { d.setViewCount((d.getViewCount() == null ? 0 : d.getViewCount()) + 1); mapper.updateById(d); }
        return R.ok().put("data", d);
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Demand>().eq("buyer_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/save")
    public R save(@RequestBody Demand d, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        d.setBuyerId(userId);
        d.setCreatedTime(new Date());
        if (d.getAuditStatus() == null) d.setAuditStatus("待审核");
        if (d.getStatus() == null) d.setStatus("征集中");
        if (d.getDemandNo() == null) d.setDemandNo("DM" + System.currentTimeMillis() + new Random().nextInt(100));
        if (d.getViewCount() == null) d.setViewCount(0);
        if (d.getAppointmentCount() == null) d.setAppointmentCount(0);
        if (d.getAcceptAppointment() == null) d.setAcceptAppointment(1);
        mapper.insert(d);
        return R.ok("需求已发布，等待审核");
    }

    @PostMapping("/audit")
    public R audit(@RequestBody Demand d) {
        Demand en = mapper.selectById(d.getId());
        if (en == null) return R.error("需求不存在");
        en.setAuditStatus(d.getAuditStatus());
        mapper.updateById(en);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody Demand d) { mapper.updateById(d); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
