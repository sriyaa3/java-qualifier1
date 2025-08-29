package com.sriyaa.bfh.java_qualifier1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class JavaQualifier1Application {

	public static void main(String[] args) {
		SpringApplication.run(JavaQualifier1Application.class, args);
	}
	@Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }
}
