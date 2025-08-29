package com.sriyaa.bfh.javaqualifier1.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "bfh")
public class BfhProps {
    private String name;
    private String regNo;
    private String email;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getRegNo() { return regNo; }
    public void setRegNo(String regNo) { this.regNo = regNo; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}