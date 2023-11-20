package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@Data
public class Deleted {
    @Column(name = "Is_Deleted")
    private boolean deleted = Boolean.FALSE;

}
