package com.sriyaa.bfh.javaqualifier1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateWebhookRequest {
    private String name;
    private String regNo;
    private String email;
}