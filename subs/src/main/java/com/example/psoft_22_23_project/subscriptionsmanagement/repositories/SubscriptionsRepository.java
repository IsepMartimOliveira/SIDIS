package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;


import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Repository
@Configuration
@Primary
public interface SubscriptionsRepository extends SubscriptionsRepoCustom,JpaRepository<Subscriptions, Long> {
    Optional<Subscriptions> findById(long id);

}

interface SubscriptionsRepoCustom {
    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;
}


@RequiredArgsConstructor
class SubscriptionsRepoCustomImpl implements SubscriptionsRepoCustom {
    @Value("${server.port}")
    private int currentPort;
    @Override
    public HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        int otherPort = (currentPort == 8082) ? 8081 : 8090;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        int otherPort = (currentPort == 8082) ? 8080 : 8093;
        URI uri = new URI("http://localhost:" + otherPort + "/api/user/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

}


