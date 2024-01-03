package com.example.loadbalancer.repository;


import com.example.loadbalancer.model.Plans;
import com.example.loadbalancer.service.PlansManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
class PlansManagerImpl implements PlansManager {
    private final LoadBalancerRepository dbRepository;

    @Override
    public void findByNameDoesNotExists(String name) {
        Optional<Plans> resultFromDB = dbRepository.findByName(name);
        if (resultFromDB.isPresent()) {
            throw new IllegalArgumentException("Plan with name " + name + " already exists!");

        }
    }

    @Override
    public Optional<Plans> findByNameDoesExists(String name) {
        Optional<Plans> resultFromDB = dbRepository.findByName(name);
        if (resultFromDB.isPresent()) {
            return resultFromDB;
        }
        throw new IllegalArgumentException("Plan with name " + name + " doesn´t exists ");

    }

    @Override
    public Plans save(Plans obj) {
        return dbRepository.save(obj);
    }


    @Override
    public Iterable<Plans> findAll() {return dbRepository.findAll();}


}
