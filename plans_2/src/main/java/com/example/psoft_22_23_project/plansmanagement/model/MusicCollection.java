package com.example.psoft_22_23_project.plansmanagement.model;


import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private Integer musicCollection;

    public void setMusicCollection(Integer musicCollection) {
        if (musicCollection == null || musicCollection < 0) {
            throw new IllegalArgumentException("Music Collection number must be positive");
        }
        this.musicCollection = musicCollection;
    }
}
