package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Disclosure;
import com.lly.mapper.DisclosureMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping("/disclosure")
public class DisclosureController {
    @Autowired private DisclosureMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String warnStatus,
                  @RequestParam(required = false) String enterpriseName) {
        QueryWrapper<Disclosure> w = new QueryWrapper<>();
        if (warnStatus != null && !warnStatus.isEmpty()) w.eq("warn_status", warnStatus);
        if (enterpriseName != null && !enterpriseName.isEmpty()) w.like("enterprise_name", enterpriseName);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Disclosure>().eq("enterprise_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/save")
    public R save(@RequestBody Disclosure d, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        if (d.getEnterpriseId() == null) d.setEnterpriseId(userId);
        d.setCreatedTime(new Date());
        d.setLastDisclosureTime(new Date());
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, 1);
        d.setNextDueTime(c.getTime());
        d.setWarnStatus("正常");
        mapper.insert(d);
        return R.ok("披露信息已提交");
    }

    @PutMapping("/update")
    public R update(@RequestBody Disclosure d) { mapper.updateById(d); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
