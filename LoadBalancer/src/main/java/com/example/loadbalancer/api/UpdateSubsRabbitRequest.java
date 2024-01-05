package com.example.loadbalancer.api;

//import com.example.psoft_22_23_project.plansmanagement.model.Plans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateSubsRabbitRequest {
        private String user;
        private long desiredVersion;
        private String auth;
        private String name;


}
