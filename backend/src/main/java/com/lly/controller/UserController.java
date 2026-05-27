package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lly.entity.User;
import com.lly.mapper.UserMapper;
import com.lly.utils.PageUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired private UserMapper mapper;

    @GetMapping("/page")
    public R page(@RequestParam(defaultValue = "1") Integer page,
                  @RequestParam(defaultValue = "10") Integer limit,
                  @RequestParam(required = false) String role,
                  @RequestParam(required = false) String keyword) {
        QueryWrapper<User> w = new QueryWrapper<>();
        if (role != null && !role.isEmpty()) w.eq("role", role);
        if (keyword != null && !keyword.isEmpty()) w.and(q -> q.like("username", keyword).or().like("real_name", keyword).or().like("phone", keyword));
        w.orderByDesc("created_time");
        return R.ok().put("data", new PageUtils(mapper.selectPage(new Page<>(page, limit), w)));
    }

    @GetMapping("/detail/{id}")
    public R detail(@PathVariable Long id) { return R.ok().put("data", mapper.selectById(id)); }

    @PostMapping("/save")
    public R save(@RequestBody User u) {
        if (mapper.selectOne(new QueryWrapper<User>().eq("username", u.getUsername())) != null) return R.error("账号已存在");
        u.setCreatedTime(new Date());
        if (u.getStatus() == null) u.setStatus(1);
        mapper.insert(u);
        return R.ok();
    }

    @PutMapping("/update")
    public R update(@RequestBody User u) { mapper.updateById(u); return R.ok(); }

    @DeleteMapping("/delete")
    public R delete(@RequestBody Long[] ids) { for (Long id : ids) mapper.deleteById(id); return R.ok(); }
}
