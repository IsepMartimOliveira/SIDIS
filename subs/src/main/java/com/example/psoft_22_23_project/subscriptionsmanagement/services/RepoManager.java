package com.example.psoft_22_23_project.subscriptionsmanagement.services;

import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubsRepoHttp;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Configuration
@Getter
public class RepoManager {
    private SubscriptionsRepository subscriptionsRepository;
    private SubsRepoHttp subsRepoHttp;

    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(boolean b, String user) {
        return subscriptionsRepository.findByActiveStatus_ActiveAndUser(b,user);
    }
    public Subscriptions save(Subscriptions obj) {
        return subscriptionsRepository.save(obj);
    }

    public HttpResponse<String> getPlansFromOtherAPI(String name) throws URISyntaxException, IOException, InterruptedException {
        return subsRepoHttp.getUserFromOtherAPI(name);
    }

    public HttpResponse<String> getUserFromOtherAPI(String newString) throws URISyntaxException, IOException, InterruptedException {
        return subsRepoHttp.getUserFromOtherAPI(newString);
    }

    public HttpResponse<String> getSubsFromOtherApi(String newString) throws URISyntaxException, IOException, InterruptedException {
        return subsRepoHttp.getUserFromOtherAPI(newString);
    }

}
