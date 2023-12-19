package com.example.psoft_22_23_project.plansmanagement.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class Bonus {
    @Column(name = "Is_Bonus")
    @SerializedName("bonus")
    private Boolean bonus;


    public void setPromoted(Boolean bonus) {
        this.bonus = bonus;
    }
    public void setPromoted(String bonus) {
        this.bonus = Boolean.valueOf(bonus);
    }

}
