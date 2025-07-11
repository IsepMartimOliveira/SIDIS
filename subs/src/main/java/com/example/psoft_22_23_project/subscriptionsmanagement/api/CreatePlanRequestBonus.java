package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class CreatePlanRequestBonus {

    private String username;
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
