package com.example.psoft_22_23_project.dashboardmanagement.api;

import com.example.psoft_22_23_project.dashboardmanagement.model.RevenueData;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class DashboardRevenueView {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The name of the plan")
    private String planName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "List of revenue data")
    private List<RevenueData> revenues;

}
