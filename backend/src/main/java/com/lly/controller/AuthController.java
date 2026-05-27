package com.lly.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.lly.entity.User;
import com.lly.mapper.UserMapper;
import com.lly.utils.JwtUtils;
import com.lly.utils.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserMapper userMapper;

    @PostMapping("/login")
    public R login(@RequestBody Map<String, String> params) {
        String username = params.get("username");
        String password = params.get("password");
        String role = params.get("role");
        if (username == null || password == null) return R.error("账号或密码不能为空");

        QueryWrapper<User> w = new QueryWrapper<>();
        w.eq("username", username).eq("password", password);
        if (role != null && !role.isEmpty()) w.eq("role", role);
        User u = userMapper.selectOne(w);
        if (u == null) return R.error("账号或密码错误");
        if (u.getStatus() != null && u.getStatus() == 0) return R.error("账号已禁用");

        String token = JwtUtils.generateToken(u.getId(), u.getUsername(), u.getRole());
        return R.ok().put("token", token).put("user", u);
    }

    @PostMapping("/register")
    public R register(@RequestBody User user) {
        if (user.getUsername() == null || user.getPassword() == null) return R.error("账号或密码不能为空");
        User existing = userMapper.selectOne(new QueryWrapper<User>().eq("username", user.getUsername()));
        if (existing != null) return R.error("账号已存在");
        if (user.getRole() == null || user.getRole().isEmpty()) user.setRole("林农");
        if (user.getStatus() == null) user.setStatus(1);
        user.setCreatedTime(new Date());
        userMapper.insert(user);
        return R.ok("注册成功");
    }

    @GetMapping("/session")
    public R session(HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        User u = userMapper.selectById(userId);
        return R.ok().put("data", u);
    }

    @PostMapping("/logout")
    public R logout() { return R.ok("已退出登录"); }

    @PutMapping("/profile")
    public R updateProfile(@RequestBody User user, HttpServletRequest req) {
        Long userId = (Long) req.getAttribute("userId");
        user.setId(userId);
        userMapper.updateById(user);
        return R.ok();
    }
}
