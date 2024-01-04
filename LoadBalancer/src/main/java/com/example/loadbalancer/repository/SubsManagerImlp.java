package com.example.loadbalancer.repository;

import com.example.loadbalancer.model.Subscriptions;
import com.example.loadbalancer.repository.SubscriptionsRepositoryDB;
import com.example.loadbalancer.service.SubsManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
class SubsManagerImlp implements SubsManager {
    private final SubscriptionsRepositoryDB dbRepository;


    public Subscriptions save(Subscriptions obj) {
        return dbRepository.save(obj);
    }

    @Override
    public Iterable<Subscriptions> findAll() {return dbRepository.findAll();}

    @Override
    public Optional<Subscriptions> findByActiveStatus_ActiveAndUser(String b, String user) {
        return dbRepository.findByActiveStatusAndUser(b,user);

    }

}
