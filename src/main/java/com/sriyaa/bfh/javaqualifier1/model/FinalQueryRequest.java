package com.sriyaa.bfh.javaqualifier1.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FinalQueryRequest {
    private String finalQuery;

    public FinalQueryRequest() {}

    public FinalQueryRequest(String finalQuery) {
        this.finalQuery = finalQuery;
    }
}