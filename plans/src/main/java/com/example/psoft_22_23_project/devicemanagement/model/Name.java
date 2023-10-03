package com.example.psoft_22_23_project.devicemanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Embeddable
public class Name {

    @Column(name = "name", nullable = false)
    @NotNull
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    public Name() {
    }
}
