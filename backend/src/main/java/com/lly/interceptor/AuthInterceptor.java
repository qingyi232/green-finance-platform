package com.lly.interceptor;

import com.lly.utils.JwtUtils;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if ("OPTIONS".equals(request.getMethod())) return true;
        String token = request.getHeader("Token");
        if (token == null || token.isEmpty()) token = request.getParameter("token");
        if (token == null || token.isEmpty()) {
            return reject(response, "请先登录");
        }
        try {
            Claims claims = JwtUtils.parseToken(token);
            request.setAttribute("userId", Long.valueOf(String.valueOf(claims.get("userId"))));
            request.setAttribute("username", String.valueOf(claims.get("username")));
            request.setAttribute("role", String.valueOf(claims.get("role")));
            return true;
        } catch (Exception e) {
            return reject(response, "登录已过期，请重新登录");
        }
    }

    private boolean reject(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"msg\":\"" + msg + "\"}");
        writer.flush();
        return false;
    }
}
