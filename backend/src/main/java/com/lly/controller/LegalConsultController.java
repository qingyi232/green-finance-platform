package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.LegalConsult;
import com.lly.mapper.LegalConsultMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/legal-consult")
public class LegalConsultController {
    @Autowired private LegalConsultMapper mapper;

    @PostMapping("/save")
    public R save(@RequestBody LegalConsult c, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        c.setApplicantId(userId);
        c.setCreatedTime(new Date());
        if (c.getStatus() == null) c.setStatus("待对接");
        if (c.getConsultNo() == null) c.setConsultNo("LC" + System.currentTimeMillis() + new Random().nextInt(100));
        mapper.insert(c);
        return R.ok("咨询已提交，对接人员将尽快联系您");
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) String consultType) {
        QueryWrapper<LegalConsult> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (consultType != null && !consultType.isEmpty()) w.eq("consult_type", consultType);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<LegalConsult>().eq("applicant_id", userId).orderByDesc("created_time")));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/handle")
    public R handle(@RequestBody LegalConsult c) {
        LegalConsult en = mapper.selectById(c.getId());
        if (en == null) return R.error("咨询不存在");
        if (c.getStatus() != null) en.setStatus(c.getStatus());
        if (c.getFeedback() != null) en.setFeedback(c.getFeedback());
        if (c.getServiceRecord() != null) en.setServiceRecord(c.getServiceRecord());
        mapper.updateById(en);
        return R.ok("已更新");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
