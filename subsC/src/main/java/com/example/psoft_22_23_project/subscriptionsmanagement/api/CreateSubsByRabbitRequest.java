package com.example.psoft_22_23_project.subscriptionsmanagement.api;

//import com.example.psoft_22_23_project.plansmanagement.model.Plans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubsByRabbitRequest {
        private CreateSubscriptionsRequest createSubscriptionsRequest;
        private String user;

}
