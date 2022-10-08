package com.example.mystore_product;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.mystore_product.mapper")
public class MystoreProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(MystoreProductApplication.class, args);
    }

}
