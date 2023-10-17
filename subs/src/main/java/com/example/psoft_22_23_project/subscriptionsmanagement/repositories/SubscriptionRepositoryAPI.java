package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@Primary
@Repository
@Configuration
public interface SubscriptionRepositoryAPI {
    HttpResponse<String> getSubsFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

}
