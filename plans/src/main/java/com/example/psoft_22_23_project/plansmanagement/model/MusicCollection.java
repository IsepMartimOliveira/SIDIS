package com.example.psoft_22_23_project.plansmanagement.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class MusicCollection {
    @Column(name = "Music_Collection")
    @NotNull
    @Min(0)
    @SerializedName("musicCollection")
    private Integer musicCollection;

    public void setMusicCollection(Integer musicCollection) {
        if (musicCollection == null || musicCollection < 0) {
            throw new IllegalArgumentException("Music Collection number must be positive");
        }
        this.musicCollection = musicCollection;
    }

    public void setMusicCollection(String musicCollection) {
        this.musicCollection = Integer.valueOf(musicCollection);
    }
}
