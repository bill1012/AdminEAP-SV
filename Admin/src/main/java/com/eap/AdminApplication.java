package com.eap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

/**
 * @Author 鬼王
 * @Date 2018/04/16 16:23
 */
@SpringBootApplication
@ServletComponentScan(basePackages = "com.eap")
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
