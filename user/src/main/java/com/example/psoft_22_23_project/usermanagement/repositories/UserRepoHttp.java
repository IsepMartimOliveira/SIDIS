package com.example.psoft_22_23_project.usermanagement.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Repository
public interface UserRepoHttp {
    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;
}
@RequiredArgsConstructor
class UserRepoHttpImpl implements UserRepoHttp {
    // 82 91 subs
    // 81 90 plans
    // 83 92 users
    @Value("${server.port}")
    private int currentPort;
    @Override
    public HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {

        int otherPort = (currentPort == 8083) ? 8092 : 8083;
        URI uri = new URI("http://localhost:" + otherPort + "/api/user/external/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }
}
