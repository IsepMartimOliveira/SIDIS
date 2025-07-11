package com.example.psoft_22_23_project.plansmanagement.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Embeddable
@Data
public class Description {
    @Column(name = "Description")
    @NotNull
    @Size(min = 1)
    @SerializedName("description")
    private String description;

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Plan");
        }
        this.description = description;
    }
}
