package com.example.psoft_22_23_project.subscriptionsmanagement.repositories;


import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
@Configuration
public interface SubscriptionsRepositoryDB extends CrudRepository<Subscriptions,Long> {
    Optional<Subscriptions> findById(long id);
    Optional<Subscriptions> findByActiveStatus_ActiveAndUser(@NotNull boolean active, @NotNull String user);
    Optional<Subscriptions> findByUser(@NotNull String user);

}

