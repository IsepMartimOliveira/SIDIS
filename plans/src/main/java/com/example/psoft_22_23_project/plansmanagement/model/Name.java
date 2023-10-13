package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@Embeddable
@Data
public class Name {
        @Column(name = "Name", unique = true)
        @NotNull
        @Size(min = 1)
        private String name;

        public void setName(String name) {
                if (name == null || name.isBlank()) {
                        throw new IllegalArgumentException("'name' is a mandatory attribute of Plan");
                }
                this.name = name;
        }


}
