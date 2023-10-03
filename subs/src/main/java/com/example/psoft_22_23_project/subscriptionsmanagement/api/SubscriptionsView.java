package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;


@Data
@Schema(description = "A Subscription")
public class SubscriptionsView {

    private String username;
    private String planName;
    private String planDescription;
    private String paymentType;
    private String startDate;
    private String endDate;
    private String activeStatus;

}
