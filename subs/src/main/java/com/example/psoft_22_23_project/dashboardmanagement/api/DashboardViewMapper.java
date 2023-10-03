package com.example.psoft_22_23_project.dashboardmanagement.api;
import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;



@Mapper(componentModel = "spring")
public abstract class DashboardViewMapper {

    @Mapping(target ="totalActiveSubscriptions" ,source = "totalActiveSubscriptions.totalActiveSubscriptions")
    @Mapping(target ="totalCanceledSubscriptions" ,source = "totalCanceledSubscriptions.totalCanceledSubscriptions")
    @Mapping(target ="totalRevenue" ,source = "totalRevenue.totalRevenue")
    @Mapping(target ="planName" ,source = "planName.planName")
    @Mapping(target ="month" ,source = "month.month")

    public abstract DashboardView toDashboardView(Dashboard dashboard);




}