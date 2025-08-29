package com.sriyaa.bfh.javaqualifier1;

import com.sriyaa.bfh.javaqualifier1.config.BfhProps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(BfhProps.class)
public class JavaQualifier1Application {

    public static void main(String[] args) {
        SpringApplication.run(JavaQualifier1Application.class, args);
    }
}