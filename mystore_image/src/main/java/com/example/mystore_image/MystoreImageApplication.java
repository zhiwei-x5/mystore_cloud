package com.example.mystore_image;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.FlushMode;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@SpringBootApplication
@MapperScan("com.example.mystore_image.mapper")
public class MystoreImageApplication {

    public static void main(String[] args) {
        SpringApplication.run(MystoreImageApplication.class, args);
    }

}
