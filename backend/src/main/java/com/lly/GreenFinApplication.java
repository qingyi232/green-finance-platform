package com.lly;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lly.mapper")
public class GreenFinApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreenFinApplication.class, args);
        System.out.println("\n  林林通·绿融通 后端服务启动成功 :: http://localhost:8080/api\n");
    }
}
