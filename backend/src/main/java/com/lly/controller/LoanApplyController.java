package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.FinanceProduct;
import com.lly.entity.LoanApply;
import com.lly.mapper.FinanceProductMapper;
import com.lly.mapper.LoanApplyMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Random;

@RestController
@RequestMapping("/loan-apply")
public class LoanApplyController {

    @Autowired private LoanApplyMapper mapper;
    @Autowired private FinanceProductMapper productMapper;

    @PostMapping("/save")
    public R save(@RequestBody LoanApply la, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        String role = (String) req.getAttribute("role");
        la.setApplicantId(userId);
        la.setApplicantRole(role);
        la.setCreatedTime(new Date());
        la.setStep("已提交");
        la.setStatus("审批中");
        la.setApplyNo("LA" + System.currentTimeMillis() + new Random().nextInt(100));
        mapper.insert(la);

        // 增加产品申请计数
        if (la.getProductId() != null) {
            FinanceProduct p = productMapper.selectById(la.getProductId());
            if (p != null) {
                p.setApplyCount((p.getApplyCount() == null ? 0 : p.getApplyCount()) + 1);
                productMapper.updateById(p);
            }
        }
        return R.ok("贷款申请已提交，银行将在 3 个工作日内审核");
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) Long bankId) {
        QueryWrapper<LoanApply> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (bankId != null) w.eq("bank_id", bankId);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<LoanApply>().eq("applicant_id", userId).orderByDesc("created_time")));
    }

    @GetMapping("/banklist")
    public R banklist(HttpServletRequest req,
                      @RequestParam(defaultValue = "1") Integer page,
                      @RequestParam(defaultValue = "10") Integer limit,
                      @RequestParam(required = false) String status) {
        Long userId = (Long) req.getAttribute("userId");
        QueryWrapper<LoanApply> w = new QueryWrapper<>();
        w.eq("bank_id", userId);
        if (status != null && !status.isEmpty()) w.eq("status", status);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/review")
    public R review(@RequestBody LoanApply la, HttpServletRequest req) {
        LoanApply en = mapper.selectById(la.getId());
        if (en == null) return R.error("申请不存在");
        en.setStep(la.getStep());
        en.setStatus(la.getStatus());
        en.setReviewMsg(la.getReviewMsg());
        en.setReviewerName((String) req.getAttribute("username"));
        en.setReviewedTime(new Date());
        if (la.getApprovedAmount() != null) en.setApprovedAmount(la.getApprovedAmount());
        if (la.getApprovedRate() != null) en.setApprovedRate(la.getApprovedRate());
        mapper.updateById(en);
        return R.ok("审批已更新");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
