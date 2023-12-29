package com.example.loadbalancer.api;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.lang.Nullable;


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





}
