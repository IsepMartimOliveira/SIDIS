package com.example.psoft_22_23_project.subscriptionsmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.api.PlansView;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.ActiveStatus;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.EndDate;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PaymentType;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.StartDate;
import com.example.psoft_22_23_project.usermanagement.api.UserView;
import com.example.psoft_22_23_project.usermanagement.model.User;
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
