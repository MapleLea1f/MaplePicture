package com.maple.maplepicture;

import org.apache.shardingsphere.spring.boot.ShardingSphereAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication(exclude = {ShardingSphereAutoConfiguration.class})
@MapperScan("com.maple.maplepicturebackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class MaplePictureBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MaplePictureBackendApplication.class, args);
    }

}
