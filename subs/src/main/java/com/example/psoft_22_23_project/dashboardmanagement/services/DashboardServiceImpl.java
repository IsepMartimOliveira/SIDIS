package com.example.psoft_22_23_project.dashboardmanagement.services;

import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
import com.example.psoft_22_23_project.dashboardmanagement.model.DisplayRevenue;
import com.example.psoft_22_23_project.dashboardmanagement.model.RevenueData;
import com.example.psoft_22_23_project.dashboardmanagement.repositories.DashboardRepository;
import com.example.psoft_22_23_project.plansmanagement.model.Plans;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private final DashboardRepository dashboardRepository;
    private final CreateDashboardMapper createDashboardMapper;
    private final PlansRepository plansRepository;


    @Override
    public Integer findTotalActiveSubscriptions(String startDate, String endDate) {
        return dashboardRepository.findTotalActiveSubscriptions(startDate, endDate);
    }

    @Override
    public Integer findTotalCanceledSubscriptions(String startDate, String endDate) {
        return dashboardRepository.findTotalCanceledSubscriptions(startDate, endDate);
    }
    @Override
    public List<Object[]> getPaymentTypeAndPlanNameCounts(String startDate, String endDate) {
        return dashboardRepository.findPaymentTypeAndPlanNameCounts(startDate,endDate);
    }

    public List<DisplayRevenue> getMonthlyRevenuePlan(String plan, Integer numberMonth) {
            if (numberMonth < 1 || numberMonth > 12) {
                throw new IllegalArgumentException("Invalid number of months. Number of months must be between 1 and 12.");
            }

            LocalDate startDate = LocalDate.now().minusYears(1);
            LocalDate endDate = LocalDate.now();
            String startDateString = startDate.toString();
            String endDateString = endDate.toString();
            List<Object[]> results = getPaymentTypeAndPlanNameCounts(startDateString, endDateString);
            List<DisplayRevenue> revenueList = new ArrayList<>();
            Month currentMonth = endDate.getMonth();
            int currentMonthNumber = currentMonth.getValue();

            for (Object[] row : results) {
                String paymentType = (String) row[0];
                String planName = (String) row[1];
                String startDateObj = (String) row[2];
                Long count = (Long) row[3];
                LocalDate startDateParse = LocalDate.parse(startDateObj);
                int subscriptionMonthNumber = startDateParse.getMonthValue();
                int monthDifference = 0;
                if (subscriptionMonthNumber > currentMonthNumber) {
                    monthDifference = 12 - (subscriptionMonthNumber - currentMonthNumber);
                } else {
                    monthDifference = 12 - (currentMonthNumber - subscriptionMonthNumber);
                }
                int monthsToAddRevenue = Math.max(monthDifference, 0);

                if (plan == null || planName.equals(plan)) {
                    Optional<Plans> optionalPlan = plansRepository.findByName_Name(planName);
                    if (optionalPlan.isPresent()) {
                        Plans planObj = optionalPlan.get();
                        Double monthlyFee = planObj.getMonthlyFee().getMonthlyFee();
                        Double annualFee = planObj.getAnnualFee().getAnnualFee();

                        Double monthlyRevenue = 0.0;
                        Double annualRevenue = 0.0;

                        if ("monthly".equals(paymentType)) {
                            monthlyRevenue = monthlyFee * count;
                        } else if ("annually".equals(paymentType)) {
                            annualRevenue = annualFee * count;
                        }

                        Double dividedAnnualRevenue = annualRevenue / 12.0;

                        generateMonthlyRevenues(planName, monthlyRevenue, dividedAnnualRevenue, numberMonth, monthsToAddRevenue, revenueList);
                    }
                }
            }

            return revenueList;
        }
    private void generateMonthlyRevenues(String planName, Double monthlyRevenue, Double dividedAnnualRevenue,
                                         Integer numberMonth, int monthsToAddRevenue, List<DisplayRevenue> revenueList) {
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
        symbols.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#.##", symbols);

        int monthsToAdd = Math.min(monthsToAddRevenue, numberMonth);
        Double totalRevenue;

        for (int i = 0; i < numberMonth; i++) {
            int monthValue = LocalDate.now().getMonthValue() + i;
            int yearOffset = monthValue / 12;
            monthValue = monthValue % 12;
            if (monthValue == 0) {
                monthValue = 12;
                yearOffset--;
            }

            Month month = Month.of(monthValue);
            String monthName = month.getDisplayName(TextStyle.FULL, Locale.ENGLISH);
            int year = LocalDate.now().getYear() + yearOffset;

            String yearString = String.valueOf(year);

            if (i == 0) {
                totalRevenue = monthlyRevenue + dividedAnnualRevenue;
            } else {
                totalRevenue = dividedAnnualRevenue;
            }
            totalRevenue = Double.parseDouble(df.format(totalRevenue));
            updatePlanData(planName, monthName, totalRevenue, yearString, df, revenueList);

            monthsToAdd--;
            if (monthsToAdd == 0) {
                break;
            }
        }
    }






    private void updatePlanData(String planName, String monthKey, Double totalRevenue, String year, DecimalFormat df, List<DisplayRevenue> revenueList) {
        for (DisplayRevenue revenue : revenueList) {
            if (revenue.getPlanName().equals(planName)) {
                List<RevenueData> revenues = revenue.getRevenues();
                for (RevenueData revenueData : revenues) {
                    if (revenueData.getMonth().equals(monthKey)) {
                        Double updatedTotalRevenue = revenueData.getTotalRevenue() + totalRevenue;
                        updatedTotalRevenue = Double.parseDouble(df.format(updatedTotalRevenue));
                        revenueData.setTotalRevenue(updatedTotalRevenue);
                        return;
                    }
                }
                RevenueData newRevenueData = new RevenueData();
                newRevenueData.setMonth(monthKey);
                newRevenueData.setTotalRevenue(totalRevenue);
                newRevenueData.setYear(year);
                revenues.add(newRevenueData);
                return;
            }
        }

        DisplayRevenue newRevenue = new DisplayRevenue();
        newRevenue.setPlanName(planName);

        RevenueData revenueData = new RevenueData();
        revenueData.setMonth(monthKey);
        revenueData.setTotalRevenue(totalRevenue);
        revenueData.setYear(year); // Set the year value

        List<RevenueData> revenues = new ArrayList<>();
        revenues.add(revenueData);
        newRevenue.setRevenues(revenues);

        revenueList.add(newRevenue);
    }








    @Override
    public Dashboard getRevenueTillNow(String plan){
        LocalDate endDate = LocalDate.now();
        int year = endDate.getYear();
        LocalDate startDate = LocalDate.of(year, Month.JANUARY, 1);
        String startDatestr=startDate.toString();
        String endDatestr=endDate.toString();
        List<Object[]> results =  getPaymentTypeAndPlanNameCounts(startDatestr,endDatestr);
        Double monthlyRevenue = 0.0;
        Double annualRevenue = 0.0;
        for (Object[] row : results) {
            String paymentType = (String) row[0];
            String planName = (String) row[1];
            Long count = (Long) row[3];
            if (plan == null || planName.equals(plan)) {
                Optional<Plans> optionalPlan = plansRepository.findByName_Name(planName);
                if (optionalPlan.isPresent()) {
                    Plans planObj = optionalPlan.get();
                    Double monthlyFee = planObj.getMonthlyFee().getMonthlyFee();
                    Double annualFee = planObj.getAnnualFee().getAnnualFee();

                    if ("monthly".equals(paymentType)) {
                        monthlyRevenue += monthlyFee * count;
                    } else if ("annually".equals(paymentType)) {
                        annualRevenue += annualFee * count;
                    }
                }
            }
        }

        Double finalRevenue = annualRevenue + monthlyRevenue;
        Dashboard obj=createDashboardMapper.createRevenueTillNow(finalRevenue);
        return obj;

    }

    @Override
    public Dashboard getDashboardView(int year, int month) {
        validateMonthAndYear(year, month);
        String formattedStartDate = getFormattedStartDate(year, month);
        String formattedEndDate = getFormattedEndDate(year, month);
        Integer totalActiveSubscriptions = findTotalActiveSubscriptions(formattedStartDate, formattedEndDate);
        Integer totalCanceledSubscriptions = findTotalCanceledSubscriptions(formattedStartDate, formattedEndDate);
        Dashboard obj = createDashboardMapper.create(totalActiveSubscriptions, totalCanceledSubscriptions);
        return obj;
    }

    private void validateMonthAndYear(int year, int month) {
        if (month < 1 || month > 12) {
            throw new IllegalArgumentException("Incorrect month. Please provide a valid month.");
        }

        if (year <= 0) {
            throw new IllegalArgumentException("Invalid year. Please provide a positive year.");
        }
    }

    public String getFormattedStartDate(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localStartDate = yearMonth.atDay(1);
        String formattedStartDate = localStartDate.toString();
        return formattedStartDate;


    }

    public String getFormattedEndDate(int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localEndDate = yearMonth.atEndOfMonth();
        String formattedEndDate = localEndDate.toString();

        return formattedEndDate;

    }

    public Dashboard getTotalNewSubscriptions(int year, int month) {
        validateMonthAndYear(year, month);
        String formattedStartDate = getFormattedStartDate(year, month);
        String formattedEndDate = getFormattedEndDate(year, month);
        Integer totalActiveSubscriptions = findTotalActiveSubscriptions(formattedStartDate, formattedEndDate);
        Dashboard obj = createDashboardMapper.createActive(totalActiveSubscriptions);
        return obj;
    }

    public Dashboard getTotalCancelledSubscriptions(int year, int month) {
        validateMonthAndYear(year, month);
        String formattedStartDate = getFormattedStartDate(year, month);
        String formattedEndDate = getFormattedEndDate(year, month);
        Integer totalCanceledSubscriptions = findTotalCanceledSubscriptions(formattedStartDate, formattedEndDate);
        Dashboard obj = createDashboardMapper.createCancelled(totalCanceledSubscriptions);
        return obj;
    }

    public Dashboard getTotalCancelledSubscriptionsByDate(int year, int month, String startDate, String endDate) {
       validateStartDateAndEndDate(startDate, endDate, year, month);

        Integer totalCanceledSubscriptions = findTotalCanceledSubscriptions(startDate, endDate);
        Dashboard obj = createDashboardMapper.createCancelled(totalCanceledSubscriptions);
        return obj;
    }

    public Dashboard getTotalNewSubscriptionsByDate(int year, int month, String startDate, String endDate) {
        validateStartDateAndEndDate(startDate, endDate, year, month);

        Integer totalActiveSubscriptions = findTotalActiveSubscriptions(startDate, endDate);
        Dashboard obj = createDashboardMapper.createActive(totalActiveSubscriptions);
        return obj;
    }
    private void validateStartDateAndEndDate(String startDate, String endDate, int year, int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        LocalDate localStartDate = yearMonth.atDay(1);
        LocalDate localEndDate = yearMonth.atEndOfMonth();
        LocalDate end = LocalDate.parse(endDate);
        LocalDate start = LocalDate.parse(startDate);
        if (localStartDate.isAfter(start)) {
            throw new IllegalArgumentException("Invalid date range. The startDate cannot be before the first day of the month.");
        }
        if (end.isAfter(localEndDate)) {
            throw new IllegalArgumentException("Invalid date range. The endDate cannot exceed the last day of the month.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Invalid date range. The endDate must be greater than or equal to the startDate.");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Invalid date range. The startDate cannot be after the endDate.");
        }
    }
}