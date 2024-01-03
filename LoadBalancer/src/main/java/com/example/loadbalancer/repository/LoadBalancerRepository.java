package com.example.loadbalancer.repository;

import com.example.loadbalancer.model.Plans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LoadBalancerRepository extends CrudRepository<Plans,Long> {
    List<Plans> findAll();

    Optional<Plans> findByName(String name);


}
