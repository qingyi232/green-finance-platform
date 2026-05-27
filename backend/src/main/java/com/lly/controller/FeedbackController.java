package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Feedback;
import com.lly.mapper.FeedbackMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {
    @Autowired private FeedbackMapper mapper;

    @PostMapping("/save")
    public R save(@RequestBody Feedback f, HttpServletRequest req) {
        try { f.setUserId((Long) req.getAttribute("userId")); } catch (Exception ignore) {}
        f.setCreatedTime(new Date());
        if (f.getStatus() == null) f.setStatus("待处理");
        mapper.insert(f);
        return R.ok("反馈已提交，我们将尽快处理");
    }

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) String type) {
        QueryWrapper<Feedback> w = new QueryWrapper<>();
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (type != null && !type.isEmpty()) w.eq("type", type);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Feedback>().eq("user_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/reply")
    public R reply(@RequestBody Map<String, Object> body, HttpServletRequest req) {
        Long id = Long.valueOf(String.valueOf(body.get("id")));
        Feedback f = mapper.selectById(id);
        if (f == null) return R.error("反馈不存在");
        f.setReplyContent((String) body.get("replyContent"));
        f.setRepliedBy((String) req.getAttribute("username"));
        f.setRepliedTime(new Date());
        f.setStatus("已回复");
        mapper.updateById(f);
        return R.ok("回复成功");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
