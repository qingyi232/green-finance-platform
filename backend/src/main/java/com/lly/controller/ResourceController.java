package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.Resource;
import com.lly.mapper.ResourceMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/resource")
public class ResourceController {
    @Autowired private ResourceMapper mapper;

    @GetMapping("/list")
    public R list(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String resourceType,
                  @RequestParam(required = false) String region,
                  @RequestParam(required = false) String status,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<Resource> w = new QueryWrapper<>();
        if (resourceType != null && !resourceType.isEmpty()) w.eq("resource_type", resourceType);
        if (region != null && !region.isEmpty()) w.like("region", region);
        if (status != null && !status.isEmpty()) w.eq("status", status);
        if (keyword != null && !keyword.isEmpty()) w.like("resource_name", keyword);
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @GetMapping("/map")
    public R map() {
        // 一张图所需：所有"已收储"资源的位置 + 类型
        List<Resource> list = mapper.selectList(new QueryWrapper<Resource>().eq("status", "已收储"));
        return R.ok().put("data", list);
    }

    @GetMapping("/mylist")
    public R mylist(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        return R.ok().put("data", mapper.selectList(new QueryWrapper<Resource>().eq("user_id", userId).orderByDesc("created_time")));
    }

    @PostMapping("/save")
    public R save(@RequestBody Resource r, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        r.setUserId(userId);
        r.setCreatedTime(new Date());
        if (r.getStatus() == null) r.setStatus("待审核");
        if (r.getResourceNo() == null) {
            r.setResourceNo("LR" + System.currentTimeMillis() + new Random().nextInt(100));
        }
        mapper.insert(r);
        return R.ok("登记申请已提交，等待审核");
    }

    @PutMapping("/update")
    public R update(@RequestBody Resource r) { mapper.updateById(r); return R.ok(); }

    @PostMapping("/audit")
    public R audit(@RequestBody Resource r, HttpServletRequest req) {
        Resource entity = mapper.selectById(r.getId());
        if (entity == null) return R.error("资源不存在");
        entity.setStatus(r.getStatus());
        entity.setAuditMsg(r.getAuditMsg());
        entity.setAuditBy((String) req.getAttribute("username"));
        entity.setAuditTime(new Date());
        mapper.updateById(entity);
        return R.ok("审核完成");
    }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
