package com.example.loadbalancer.services;

import com.example.loadbalancer.api.CreatePlanRequest;
import com.example.loadbalancer.api.CreatePlanRequestBonus;
import com.example.loadbalancer.model.Plans;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CreatePlanMapper {
    public abstract Plans create(CreatePlanRequest request);

    public abstract CreatePlanRequest createInverse(Plans request);

    public abstract Plans createBonus(CreatePlanRequestBonus request);
}
