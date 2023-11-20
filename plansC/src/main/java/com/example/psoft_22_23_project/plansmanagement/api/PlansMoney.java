package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")

public abstract class  PlansMoney {



    @Mapping(source = "planMoneyRequest.annualFee", target = "annualFee.annualFee")
    @Mapping(source = "planMoneyRequest.monthlyFee", target = "monthlyFee.monthlyFee")
    public  abstract Plans toPlansVieMoney(EditPlanMoneyRequest planMoneyRequest);
}
