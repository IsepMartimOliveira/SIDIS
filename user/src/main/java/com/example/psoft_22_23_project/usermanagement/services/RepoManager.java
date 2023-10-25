package com.example.psoft_22_23_project.usermanagement.services;

import com.example.psoft_22_23_project.usermanagement.model.User;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepoHttp;
import com.example.psoft_22_23_project.usermanagement.repositories.UserRepository;
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
    private final UserRepository userRepository;

    private final UserRepoHttp userRepoHttp;
    public User save(User user) {
        return userRepository.save(user);
    }

    public Iterable<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public HttpResponse<String> getUserFromOtherAPI(String username) throws URISyntaxException, IOException, InterruptedException {
        return userRepoHttp.getUserFromOtherAPI(username);
    }
}
