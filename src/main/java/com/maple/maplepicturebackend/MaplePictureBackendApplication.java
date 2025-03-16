package com.maple.maplepicturebackend;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@MapperScan("com.maple.maplepicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class MaplePictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaplePictureBackendApplication.class, args);
    }

}
