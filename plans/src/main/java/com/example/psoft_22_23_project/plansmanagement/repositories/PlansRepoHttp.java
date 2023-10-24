package com.example.psoft_22_23_project.plansmanagement.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
@Repository
public interface PlansRepoHttp {
    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;
    HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> doPlansPatchAPI(String name, final long desiredVersion, String json) throws URISyntaxException, IOException, InterruptedException;
    HttpResponse<String> doPlansPatchMoneyAPI(String name, final long desiredVersion, String json)throws URISyntaxException, IOException, InterruptedException;
    HttpResponse<String> doPlansPatchDeactivate(String name, final long desiredVersion)throws URISyntaxException, IOException, InterruptedException;

}
@RequiredArgsConstructor
@Configuration
class PlansRepoHttpCustomImpl implements PlansRepoHttp {
    @Value("${server.port}")
    private int currentPort;
    @Override
    public HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {

        int otherPort = (currentPort == 8081) ? 8090 : 8081;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }


    @Override
    public HttpResponse<String> getPlansFromOtherAPI() throws URISyntaxException, IOException, InterruptedException {

        int otherPort = (currentPort == 8081) ? 8090 : 8081;
        URI uri = new URI("http://localhost:" + otherPort + "/api/plans/external");

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public HttpResponse<String> doPlansPatchMoneyAPI(String name, final long desiredVersion, String json) throws URISyntaxException, IOException, InterruptedException {
        int otherPort = (currentPort == 8081) ? 8090 : 8081;
        String apiUrl = "http://localhost:" + otherPort + "/api/plans/updateMoney/" + name;
        HttpRequest requestpatch = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("if-match", Long.toString(desiredVersion))
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

        return responses;

    }

    @Override
    public HttpResponse<String> doPlansPatchAPI(String name, final long desiredVersion, String json) throws URISyntaxException, IOException, InterruptedException {
        int otherPort = (currentPort == 8081) ? 8090 : 8081;
        String apiUrl = "http://localhost:" + otherPort + "/api/plans/update/" + name;

        HttpRequest requestpatch = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("if-match", Long.toString(desiredVersion) )
                .method("PATCH", HttpRequest.BodyPublishers.ofString(json))
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

        return responses;

    }

    @Override
    public HttpResponse<String> doPlansPatchDeactivate(String name, long desiredVersion) throws URISyntaxException, IOException, InterruptedException {
        int otherPort = (currentPort == 8081) ? 8090 : 8081;
        String apiUrl = "http://localhost:" + otherPort + "/api/plans/deactivate/" + name;

        HttpRequest requestpatch = HttpRequest.newBuilder()
                .uri(URI.create(apiUrl))
                .header("Content-Type", "application/json")
                .header("if-match", Long.toString(desiredVersion))
                .method("PATCH", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpClient httpClient = HttpClient.newHttpClient();
        HttpResponse<String> responses = httpClient.send(requestpatch, HttpResponse.BodyHandlers.ofString());

        return responses;
    }



}

