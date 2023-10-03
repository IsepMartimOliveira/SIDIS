package com.example.psoft_22_23_project.dashboardmanagement.model;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import java.util.List;


@Getter
@Setter
public class DisplayRevenue {
    private List<RevenueData> revenues;
    private String planName;

}



