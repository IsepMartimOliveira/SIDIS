package com.example.psoft_22_23_project.dashboardmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class TotalCanceledSubscriptions {
    @NotNull
    private Integer totalCanceledSubscriptions;

}
