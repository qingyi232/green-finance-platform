package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.EvaluationApply;
import com.lly.mapper.EvaluationApplyMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/evaluation-apply")
public class EvaluationApplyController {
    @Autowired private EvaluationApplyMapper mapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) String step) {
        QueryWrapper<EvaluationApply> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (step != null && !step.isEmpty()) w.eq("step", step);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<EvaluationApply>().eq("applicant_id", userId).orderByDesc("created_time")));
    }

    @GetMapping("/orglist")
    public R orglist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<EvaluationApply>().eq("org_id", userId).orderByDesc("created_time")));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody EvaluationApply e, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        e.setApplicantId(userId);
        e.setCreatedTime(new Date());
        if (e.getStep() == null) e.setStep("材料上报");
        if (e.getStatus() == null) e.setStatus("待对接");
        if (e.getApplyNo() == null) e.setApplyNo("EA" + System.currentTimeMillis() + new Random().nextInt(100));
        mapper.insert(e);
        return R.ok("评估申请已提交，机构将在 1-3 个工作日内对接");
    }

    @PostMapping("/handle")
    public R handle(@RequestBody EvaluationApply e) {
        EvaluationApply en = mapper.selectById(e.getId());
        if (en == null) return R.error("申请不存在");
        if (e.getStep() != null) en.setStep(e.getStep());
        if (e.getStatus() != null) en.setStatus(e.getStatus());
        if (e.getResultValue() != null) en.setResultValue(e.getResultValue());
        if (e.getResultSummary() != null) en.setResultSummary(e.getResultSummary());
        if (e.getFeedback() != null) en.setFeedback(e.getFeedback());
        mapper.updateById(en);
        return R.ok("已更新");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
