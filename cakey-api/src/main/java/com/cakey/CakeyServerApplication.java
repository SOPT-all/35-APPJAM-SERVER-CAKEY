package com.cakey;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@EnableFeignClients
@SpringBootApplication
@ImportAutoConfiguration(FeignAutoConfiguration.class)
public class CakeyServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(CakeyServerApplication.class, args);
    }
}