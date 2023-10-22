package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;


import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Optional;

@Repository
@Configuration
@CacheConfig(cacheNames = "subs")
@Primary
public interface SubscriptionsRepository extends SubscriptionsRepoCustom,JpaRepository<Subscriptions, Long> {
    Optional<Subscriptions> findById(long id);
    Optional<Subscriptions> findByActiveStatus_ActiveAndUser(@NotNull boolean active, @NotNull String user);

}

interface SubscriptionsRepoCustom {
    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getSubsFromOtherApi(String name) throws URISyntaxException, IOException, InterruptedException;

}


@RequiredArgsConstructor
class SubscriptionsRepoCustomImpl implements SubscriptionsRepoCustom {
    @Value("${server.port}")
    private int currentPort;
    @Override
    public HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
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
        int otherPort = (currentPort == 8082) ? 8083 : 8093;
        URI uri = new URI("http://localhost:" + otherPort + "/api/user/" + name);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }

    @Override
    public HttpResponse<String> getSubsFromOtherApi(String userName) throws URISyntaxException, IOException, InterruptedException {
        // 82 91 subs
        // 81 90 plans
        // 83 92 users
        int otherPort = (currentPort == 8082) ? 8091 : 8082;

        URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions2/" + userName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                //.header("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJleGFtcGxlLmlvIiwic3ViIjoiNCxhbGV4QG1haWwuY29tIiwiZXhwIjoxNjk4MDA5OTc1LCJpYXQiOjE2OTc5NzM5NzUsInJvbGVzIjoiU3Vic2NyaWJlciJ9.X27Z8gvixiP1R7iqNSAXn1bSeTdS5HmxK0ZNIo7negqA_WXun_9VVjex06E_oW2qlMEKINsmXd4L0Ycjr4O88RpKo9gTLy0bQxOwTAIbALfeZ7K3uaCSFfT0d_2ngxeLKwvRTxYqhQddcUYqW9N347PNHQP9XcR7_pwBPTQH0CpFHb8ue39mlg_ynPKnU7GbhLQ1uZXg9XhIBqB0DlZiBm1HLta3exCTPfTCAvPi2UVWJ-OqotbTKT3fj8v5DEBkXfFAmP4DZUTGpdV4cpuNEHimf5j4esUWHrR1qWh1GaH6zgPOvV5Y04DgSpJvmXIv05Cdrsduqgz3kTriu2Ze5kdA75OJN83g-Wl3LNzAkU4lL4lEnZnaHECeByPoAmrBl3VqN3Ww7RBhUMUwM9CZD45R5-kC0S6Na-NXOgYPhuI9eAWv937ikXzxsBzTYFUwuWd06-JBoZbwAf0bYG_7x8ccF_9kpkEbJhccuihgjFvHsbxoTrZZOFUdVzqvkdrwnLOqqJzK323-U0zvHwqQ3iKAbA-elyl2KDYkDC4HPZX1zUtm5jvO2AhoMbp8gFJFLT3R534Nh41iJ1_YZyP-n1ud0SbUJKnXf8tQgiRi1dH3ZMWTQqVwlNBfOSf6SLzsvK1o9R26U7gBFTVtdfgNNiwEXaHewfwAzZr-UllX5RA") // Add the token to the Authorization header
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }
}


