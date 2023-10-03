package com.example.psoft_22_23_project.plansmanagement.model;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
@Embeddable
@Data
public class Active {
    @Column(name = "Is_Active")
    private Boolean active;


}
