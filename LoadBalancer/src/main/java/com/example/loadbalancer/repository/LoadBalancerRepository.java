package com.example.loadbalancer.repository;

import com.example.loadbalancer.model.Plans;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoadBalancerRepository extends CrudRepository<Plans,Long> {
    List<Plans> findAll();
}
