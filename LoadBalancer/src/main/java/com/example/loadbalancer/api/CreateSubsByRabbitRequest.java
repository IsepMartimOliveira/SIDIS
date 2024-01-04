package com.example.loadbalancer.api;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateSubsByRabbitRequest {
        private CreateSubscriptionsRequest createSubscriptionsRequest;
        private String user;

}
