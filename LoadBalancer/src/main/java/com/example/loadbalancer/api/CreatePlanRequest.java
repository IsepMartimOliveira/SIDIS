package com.example.loadbalancer.api;

import lombok.Data;

@Data
public class CreatePlanRequest {


    private String name;

    private String description;

    private String numberOfMinutes;

    private String musicSuggestion;

    private Integer maximumNumberOfUsers;

    private Integer musicCollection;

    private double annualFee;

    private double monthlyFee;

    private boolean active;
    private boolean promoted;
}
