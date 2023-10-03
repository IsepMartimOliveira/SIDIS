package com.example.psoft_22_23_project.plansmanagement.model;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private String description;

    public void setDescription(String description) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description is a mandatory attribute of Plan");
        }
        this.description = description;
    }
}
