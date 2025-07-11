package com.example.loadbalancer.api;

import lombok.Data;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


@Data
public class CreatePlanRequestBonus {
    @NotNull
    private String username;
    @Size(min = 1)
    @NotNull
    private String name;
    @Size(min = 1)
    @NotNull
    private String description;
    @Size(min = 1)
    @NotNull
    private String numberOfMinutes;
    @Pattern(regexp = "(automatic|personalized)")
    @NotNull
    private String musicSuggestion;
    @Min(0)
    @NotNull
    private Integer maximumNumberOfUsers;
    @Min(0)
    @NotNull
    private Integer musicCollection;
    @Min(0)
    @NotNull
    private double annualFee;
    @Min(0)
    @NotNull
    private double monthlyFee;
    @Nullable
    private boolean active;
    @Nullable
    private boolean promoted;
    @NotNull
    private String paymentType;




}
