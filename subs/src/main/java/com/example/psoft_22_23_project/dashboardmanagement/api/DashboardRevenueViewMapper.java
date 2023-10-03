package com.example.psoft_22_23_project.dashboardmanagement.api;

import com.example.psoft_22_23_project.dashboardmanagement.model.DisplayRevenue;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class DashboardRevenueViewMapper {
    @Mapping(source = "planName", target = "planName")
    @Mapping(source = "revenues", target = "revenues")
    public abstract DashboardRevenueView toDashboardRevenueView(DisplayRevenue displayRevenue);
}

