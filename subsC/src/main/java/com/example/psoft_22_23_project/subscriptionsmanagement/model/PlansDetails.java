package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PlansDetails {
    private String name;
    private String description;
    private String numberOfMinutes;
    private String maximumNumberOfUsers;
    private String musicCollection;
    private String musicSuggestion;
    private String annualFee;
    private String monthlyFee;
    private String active;
    private String promoted;

    public PlansDetails(String name, String description, String numberOfMinutes, String maximumNumberOfUsers, String musicCollection, String musicSuggestion, String annualFee, String monthlyFee, String active, String promoted) {
        this.name = name;
        this.description = description;
        this.numberOfMinutes = numberOfMinutes;
        this.maximumNumberOfUsers = maximumNumberOfUsers;
        this.musicCollection = musicCollection;
        this.musicSuggestion = musicSuggestion;
        this.annualFee = annualFee;
        this.monthlyFee = monthlyFee;
        this.active = active;
        this.promoted = promoted;
    }
}
