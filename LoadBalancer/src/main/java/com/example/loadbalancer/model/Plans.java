package com.example.loadbalancer.model;



import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "Plans")
public class Plans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    public Plans(String name, String description, String numberOfMinutes, String maximumNumberOfUsers, String musicCollection, String musicSuggestion, String annualFee, String monthlyFee, String active, String promoted) {
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


    protected  Plans(){}

    public void updateData( String description,
                           String maximumNumberOfUsers,
                           String numberOfMinutes , String musicCollections,
                           String musicSuggestions, String active, String promoted) {


        if(description != null){
            this.description=(description);
        }

        if(maximumNumberOfUsers != null){
            this.maximumNumberOfUsers=(maximumNumberOfUsers);
        }

        if(musicCollections != null){
            this.musicCollection=(musicCollections);
        }

        if(numberOfMinutes != null){
            this.numberOfMinutes=(numberOfMinutes);
        }

        if(musicSuggestions != null){
            this.musicSuggestion=(musicSuggestions);
        }
        if(active != null){
            this.active=(active);
        }
        if(promoted != null){
            this.promoted=(promoted);
        }


    }
    public void deactivate() {

        this.active=("false");
    }

}
