package com.example.psoft_22_23_project.plansmanagement.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
@Embeddable
@Data
public class Active {
    @Column(name = "Is_Active")
    @SerializedName("active")
    private Boolean active;
    public void setActive(String active) {
        this.active = Boolean.valueOf(active);
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
