package com.lly.config;

import com.lly.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Value("${file.upload-path:./uploads/}")
    private String uploadPath;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        // 认证类
                        "/auth/login", "/auth/register", "/auth/captcha",
                        // 公开内容
                        "/news/list", "/news/detail/**", "/news/hot",
                        "/policy/list", "/policy/detail/**",
                        "/resource/list", "/resource/detail/**", "/resource/map",
                        "/listing/list", "/listing/detail/**",
                        "/demand/list", "/demand/detail/**",
                        "/farm-product/list", "/farm-product/detail/**",
                        "/finance-product/list", "/finance-product/detail/**",
                        "/finance-product/match",
                        "/evaluation-org/list", "/evaluation-org/detail/**",
                        "/evaluator/list", "/evaluator/detail/**",
                        "/legal-org/list", "/legal-org/detail/**",
                        "/lawyer/list", "/lawyer/detail/**",
                        "/disclosure/list", "/disclosure/detail/**",
                        "/feedback/save",
                        "/stats/**",
                        // 静态资源
                        "/file/**", "/uploads/**",
                        // swagger / 健康检查
                        "/error"
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:" + uploadPath);
    }
}
