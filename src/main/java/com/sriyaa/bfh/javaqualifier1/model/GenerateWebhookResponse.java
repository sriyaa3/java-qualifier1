package com.sriyaa.bfh.javaqualifier1.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateWebhookResponse {
    private String webhook;
    private String accessToken;
}