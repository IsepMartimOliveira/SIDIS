package com.example.psoft_22_23_project.plansmanagement.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Embeddable
@Data
public class MusicSuggestion {
    @Column(name = "Music_Suggestion")
    @NotNull
    @Pattern(regexp = "(automatic|personalized)")
    @SerializedName("musicSuggestion")
    private String musicSuggestion;

    public void setMusicSuggestion(String musicSuggestion) {
        if (musicSuggestion.equals("automatic") || musicSuggestion.equals("personalized")){
            this.musicSuggestion = musicSuggestion;

        }else throw new IllegalArgumentException("Music Suggestion can only be 'personalized' or 'automatic' ");

    }


}
