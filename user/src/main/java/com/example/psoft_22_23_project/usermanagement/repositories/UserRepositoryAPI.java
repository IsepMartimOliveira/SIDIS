package com.example.psoft_22_23_project.usermanagement.repositories;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;

@Repository
@Configuration
@Primary

public interface UserRepositoryAPI {
    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;
}
