package com.example.loadbalancer.repository;

import com.example.loadbalancer.model.Plans;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public interface LoadBalancerRepository extends CrudRepository<Plans,Long> {
    List<Plans> findAll();

    Optional<Plans> findByName(String name);


    @Modifying
    @Transactional
    @Query("DELETE FROM Plans p WHERE p.name = :name")
    void deletePlansByName(@NotNull @Param("name") String name);
}
