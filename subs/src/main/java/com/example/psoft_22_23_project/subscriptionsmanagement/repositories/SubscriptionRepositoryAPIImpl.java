package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Repository

public class SubscriptionRepositoryAPIImpl implements SubscriptionRepositoryAPI{
    @Value("${server.port}")
    private int currentPort;
    @Override
    public HttpResponse<String> getSubsFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
       //Falta implementar o endpoint
        int otherPort = (currentPort == 8082) ? 8091 : 8082;
        URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;
    }
}
