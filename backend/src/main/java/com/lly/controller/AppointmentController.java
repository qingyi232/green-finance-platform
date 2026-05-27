package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Appointment;
import com.lly.mapper.AppointmentMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@RestController
@RequestMapping("/appointment")
public class AppointmentController {
    @Autowired private AppointmentMapper mapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) String targetType) {
        QueryWrapper<Appointment> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (targetType != null && !targetType.isEmpty()) w.eq("target_type", targetType);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Appointment>().eq("applicant_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/save")
    public R save(@RequestBody Appointment a, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        a.setApplicantId(userId);
        a.setCreatedTime(new Date());
        if (a.getStatus() == null) a.setStatus("待回复");
        mapper.insert(a);
        return R.ok("预约已提交，对方将尽快回复");
    }

    @PostMapping("/reply")
    public R reply(@RequestBody Appointment a) {
        Appointment en = mapper.selectById(a.getId());
        if (en == null) return R.error("预约不存在");
        en.setReply(a.getReply());
        en.setStatus(a.getStatus());
        mapper.updateById(en);
        return R.ok();
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
