package com.example.psoft_22_23_project.dashboardmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;


@Embeddable
@Getter
@Setter
@Data
@RequiredArgsConstructor
public class TotalRevenue {
    @NotNull
    private Double totalRevenue;

}
