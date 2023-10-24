package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Configuration
public interface SubsRepoHttp {
    HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getUserFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException;

    HttpResponse<String> getSubsFromOtherApi(String name) throws URISyntaxException, IOException, InterruptedException;

}
@RequiredArgsConstructor
class SubsRepoHttpCustomImpl implements SubsRepoHttp {
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
        int otherPort = (currentPort == 8082) ? 8083 : 8092;
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

        URI uri = new URI("http://localhost:" + otherPort + "/api/subscriptions/external/" + userName);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                //.header("Authorization", "Bearer eyJhbGciOiJSUzI1NiJ9.eyJpc3MiOiJleGFtcGxlLmlvIiwic3ViIjoiNCxhbGV4QG1haWwuY29tIiwiZXhwIjoxNjk4MDQzMjU4LCJpYXQiOjE2OTgwMDcyNTgsInJvbGVzIjoiU3Vic2NyaWJlciJ9.DoaA-_sJI9FLZUm8PmgAzGhuaHU-Scjl0uQlFuOZ3H8nl1fxMaSeW9hNqPO66ok5bdAITKCQDDgfv-YHgfq5vPqKGhLDaX1Ec98ATckibnJW08Xojh8FWEdyEw-YXOHAjLLDJ5OYK7CebF7NQDC8W8s_DQHuNYMZeISi-MPTsm9NGd6Qwcbo6Ic42JfgMu8Kt_0AlyaR6AtLLubFK3M0xm4P3bSwiDNW-jTTHM9mmNUJv46dHph-ZmQSaxowPIotR9x3pkKCLFHnx1U1Ob8Cf63qGqWMBV-3xSQ_j75SgK7oG0dslaCiqfbsl-GwMlatMJjH1931ktaOiCKrLnALDA75FR1tXADmQVjigpBPFvuKVcX3PvMGRteuapcNd3xo58Fss7x-fOvujmJSRJ-MbHvnoH5wRUI7WoiugqcDPF9QuNDBPEQUaE70xvDUtBnPpDE9crQDObDw6NRn-C1WUlYDw0aNdAMWGMP74Bx-gCeIE-dZsZchMifYVhgsqsO68uM_xk4G_gRWkHgL67vMNGlrj09JLHkJcKbrNLL2Ey9tyFTBsvVrYfWu8-pgqV-l004I3rulCjtcMX7TR9lwwzgAhH_IKXTZIFWyS0xhWPRMfnftCsxG8oli4Pd55nUCV7CBT2fls_M7tHRoyqnGTnPaqVXjD2Cplc5wh2uMoT8")
                .build();

        HttpClient client = HttpClient.newHttpClient();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        return response;

    }
}

