package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Listing;
import com.lly.mapper.ListingMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/listing")
public class ListingController {
    @Autowired private ListingMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "12") Integer limit,
                  @RequestParam(required = false) String category,
                  @RequestParam(required = false) String subCategory,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<Listing> w = new QueryWrapper<>();
        w.eq("status", "挂牌中").eq("audit_status", "已通过");
        if (category != null && !category.isEmpty()) w.eq("category", category);
        if (subCategory != null && !subCategory.isEmpty()) w.eq("sub_category", subCategory);
        if (keyword != null && !keyword.isEmpty()) w.like("title", keyword);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) {
        Listing l = mapper.selectById(id);
        if (l != null) { l.setViewCount((l.getViewCount() == null ? 0 : l.getViewCount()) + 1); mapper.updateById(l); }
        return R.ok().put("data", l);
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Listing>().eq("seller_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/save")
    public R save(@RequestBody Listing l, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        l.setSellerId(userId);
        l.setCreatedTime(new Date());
        if (l.getAuditStatus() == null) l.setAuditStatus("待审核");
        if (l.getStatus() == null) l.setStatus("挂牌中");
        if (l.getListingNo() == null) l.setListingNo("LIST" + System.currentTimeMillis() + new Random().nextInt(100));
        if (l.getViewCount() == null) l.setViewCount(0);
        if (l.getAppointmentCount() == null) l.setAppointmentCount(0);
        if (l.getAcceptAppointment() == null) l.setAcceptAppointment(1);
        mapper.insert(l);
        return R.ok("挂牌信息已提交，等待审核");
    }

    @PostMapping("/audit")
    public R audit(@RequestBody Listing l) {
        Listing en = mapper.selectById(l.getId());
        if (en == null) return R.error("挂牌不存在");
        en.setAuditStatus(l.getAuditStatus());
        en.setAuditMsg(l.getAuditMsg());
        mapper.updateById(en);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody Listing l) { mapper.updateById(l); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
