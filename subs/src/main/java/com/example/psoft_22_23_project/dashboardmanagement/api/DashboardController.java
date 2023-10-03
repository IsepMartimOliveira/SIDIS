package com.example.psoft_22_23_project.dashboardmanagement.api;
import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
import com.example.psoft_22_23_project.dashboardmanagement.model.DisplayRevenue;
import com.example.psoft_22_23_project.dashboardmanagement.services.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
public class DashboardController {


    private final DashboardService dashboardService;
    private final DashboardViewMapper dashboardViewMapper;
    private final DashboardRevenueViewMapper dashboardRevenueViewMapper;


    @Operation(summary = "Gets total active and canceled subscriptions")
    @GetMapping("/status")
    public DashboardView getDashboardView(
            @RequestParam("year") Integer year,
            @RequestParam("month") Integer month,
            @RequestParam(value = "onlyActive", defaultValue = "false") boolean onlyActive,
            @RequestParam(value = "onlyCanceled", defaultValue = "false") boolean onlyCanceled,@RequestParam(value = "startDate", required = false) String startDate,
            @RequestParam(value = "endDate", required = false) String endDate
    ) {
        if (onlyActive && !onlyCanceled)
            if (endDate != null && startDate != null) {
                Dashboard dashboard1 = dashboardService.getTotalNewSubscriptionsByDate(year, month, startDate, endDate);
                return dashboardViewMapper.toDashboardView(dashboard1);
            } else {
                Dashboard dashboard = dashboardService.getTotalNewSubscriptions(year, month);
                return dashboardViewMapper.toDashboardView(dashboard);
            }

        if (onlyCanceled && !onlyActive) {
            if (endDate != null && startDate != null) {
                Dashboard dashboard1 = dashboardService.getTotalCancelledSubscriptionsByDate(year, month, startDate, endDate);
                return dashboardViewMapper.toDashboardView(dashboard1);
            } else {
                Dashboard dashboard = dashboardService.getTotalCancelledSubscriptions(year, month);
                return dashboardViewMapper.toDashboardView(dashboard);
            }
        }

        Dashboard dashboard = dashboardService.getDashboardView(year, month);
        return dashboardViewMapper.toDashboardView(dashboard);
    }
    @Operation(summary = "Gets future revenue")
    @GetMapping("/revenuePlan")
    public List<DashboardRevenueView> getDashboardRevenue(
            @RequestParam(value = "plan", required = false) String plan,
            @RequestParam("numberMonth") Integer numberMonth) {
        List<DisplayRevenue> displayRevenues = dashboardService.getMonthlyRevenuePlan(plan, numberMonth);

        List<DashboardRevenueView> dashboardRevenueViews = displayRevenues.stream()
                .map(displayRevenue -> dashboardRevenueViewMapper.toDashboardRevenueView(displayRevenue))
                .collect(Collectors.toList());

        return dashboardRevenueViews;
    }

    @Operation(summary = "Gets all revenue till now")
    @GetMapping("/currentRevenue")
    public DashboardView getDashboardRevenueTillNow( @RequestParam(value = "plan", required = false) String plan){
        Dashboard dashboard=dashboardService.getRevenueTillNow(plan);
        return dashboardViewMapper.toDashboardView(dashboard);


    }
    }










