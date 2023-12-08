package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "PlanDetails")
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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    public void updateDetails(String description, String numberOfMinutes,
                              String maximumNumberOfUsers, String musicCollection,
                              String musicSuggestion, String annualFee, String monthlyFee,
                              String active, String promoted) {
        if (description != null) {
            this.description = description;
        }

        if (numberOfMinutes != null) {
            this.numberOfMinutes = numberOfMinutes;
        }

        if (maximumNumberOfUsers != null) {
            this.maximumNumberOfUsers = maximumNumberOfUsers;
        }

        if (musicCollection != null) {
            this.musicCollection = musicCollection;
        }

        if (musicSuggestion != null) {
            this.musicSuggestion = musicSuggestion;
        }

        if (annualFee != null) {
            this.annualFee = annualFee;
        }

        if (monthlyFee != null) {
            this.monthlyFee = monthlyFee;
        }

        if (active != null) {
            this.active = active;
        }

        if (promoted != null) {
            this.promoted = promoted;
        }
    }

}
