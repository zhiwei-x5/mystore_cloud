package com.example.mystore_detailed;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mystore_detailed.mapper")
public class MystoreDetailedApplication {

    public static void main(String[] args) {
        SpringApplication.run(MystoreDetailedApplication.class, args);
    }

}
