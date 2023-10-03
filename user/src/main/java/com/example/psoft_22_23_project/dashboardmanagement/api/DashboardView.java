package com.example.psoft_22_23_project.dashboardmanagement.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;




@Data
public class DashboardView {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The total number of active subscriptions in the system")
    private  Integer totalActiveSubscriptions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The total number of canceled subscriptions in the system")
    private  Integer totalCanceledSubscriptions;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The total number of canceled subscriptions in the system")
    private  Double totalRevenue;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The total number of canceled subscriptions in the system")
    private  String planName;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @Schema(description = "The total number of canceled subscriptions in the system")
    private  String month;

    public DashboardView(Integer totalActiveSubscriptions, Integer totalCanceledSubscriptions,Double totalRevenue,String planName,String month) {
        this.totalActiveSubscriptions = totalActiveSubscriptions;
        this.totalCanceledSubscriptions = totalCanceledSubscriptions;
        this.totalRevenue=totalRevenue;
        this.planName=planName;
        this.month=month;
    }

}



