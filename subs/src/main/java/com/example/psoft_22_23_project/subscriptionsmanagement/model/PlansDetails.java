package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.StaleObjectStateException;

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
    @Version
    private long version;

    public void updateData(long desiredVersion, String description,
                           Integer maximumNumberOfUsers,
                           String numberOfMinutes , Integer musicCollections,
                           String musicSuggestions, Boolean active, Boolean promoted) {

        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if(description != null){
            this.description = (description);
        }

        if(maximumNumberOfUsers != null){
            this.maximumNumberOfUsers = String.valueOf((maximumNumberOfUsers));
        }

        if(musicCollections != null){
            this.musicCollection = String.valueOf((musicCollections));
        }

        if(numberOfMinutes != null){
            this.numberOfMinutes = (numberOfMinutes);
        }

        if(musicSuggestions != null){
            this.musicSuggestion = (musicSuggestions);
        }
        if(active != null){
            this.active = String.valueOf((active));
        }
        if(promoted != null){
            this.promoted = String.valueOf((promoted));
        }


    }
    public void deactivate(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }

        this.active = String.valueOf((false));
    }

}
