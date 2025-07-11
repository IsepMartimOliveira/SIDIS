package com.example.psoft_22_23_project.plansmanagement.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class Promoted {
    @Column(name = "Is_Promoted")
    @NotNull
    @SerializedName("promoted")
    private Boolean promoted;

    public void setPromoted(Boolean promoted) {
        this.promoted = promoted;
    }
    public void setPromoted(String promoted) {
        this.promoted = Boolean.valueOf(promoted);
    }

}
