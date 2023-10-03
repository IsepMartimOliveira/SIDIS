package com.example.psoft_22_23_project.dashboardmanagement.services;
import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
import com.example.psoft_22_23_project.dashboardmanagement.model.DisplayRevenue;
import java.util.List;

public interface DashboardService {
    Integer findTotalActiveSubscriptions(String startDate,String endDate);
    Integer findTotalCanceledSubscriptions(String startDate,String endDate);
    Dashboard getDashboardView(int year, int month);
    Dashboard getTotalNewSubscriptions(int year, int month);
    Dashboard getTotalCancelledSubscriptions(int year, int month);
    Dashboard getTotalCancelledSubscriptionsByDate(int year,int month,String startDate, String endDate);
    Dashboard getTotalNewSubscriptionsByDate(int year, int month, String startDate, String endDate);
    List<Object[]> getPaymentTypeAndPlanNameCounts(String startDate,String endDate);
    Dashboard getRevenueTillNow(String plan);
    List<DisplayRevenue> getMonthlyRevenuePlan(String plan, Integer numberMonth);

}
