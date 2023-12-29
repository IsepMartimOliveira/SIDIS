package com.example.loadbalancer.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;


@Table(name = "Plans")
@Getter
@Setter
@Entity
public class Plans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Version
    private long version;
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

    public Plans(String name, String description,
                 String numberOfMinutes, String maximumNumberOfUsers
                , String musicCollection, String musicSuggestion
                , String annualFee, String monthlyFee
                , String active, String promoted) {
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

    protected Plans() {

    }

    public void updateData(long desiredVersion, String description,
                           String maximumNumberOfUsers,
                           String numberOfMinutes , String musicCollections,
                           String musicSuggestions, String active, String promoted) {

        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
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

    public void deactivate(final long desiredVersion) {
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }

        this.active=("false");
    }

}
