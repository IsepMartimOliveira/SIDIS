package DashboardTest.Service;

import com.example.psoft_22_23_project.dashboardmanagement.model.*;
import com.example.psoft_22_23_project.dashboardmanagement.repositories.DashboardRepository;
import com.example.psoft_22_23_project.dashboardmanagement.services.CreateDashboardMapper;
import com.example.psoft_22_23_project.dashboardmanagement.services.DashboardService;
import com.example.psoft_22_23_project.dashboardmanagement.services.DashboardServiceImpl;
import com.example.psoft_22_23_project.plansmanagement.model.*;
import com.example.psoft_22_23_project.plansmanagement.repositories.PlansRepository;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.PaymentType;
import com.example.psoft_22_23_project.subscriptionsmanagement.model.Subscriptions;
import com.example.psoft_22_23_project.subscriptionsmanagement.repositories.SubscriptionsRepository;
import com.example.psoft_22_23_project.usermanagement.model.User;
import org.h2.table.Plan;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DashboardServiceImplTest {
    @Mock
    private SubscriptionsRepository subscriptionsRepository;

    @Mock
    private DashboardRepository dashboardRepository;
    @Mock
    private CreateDashboardMapper createDashboardMapper;

    @Mock
    private PlansRepository plansRepository;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @Captor
    private ArgumentCaptor<String> planNameCaptor;

    @Captor
    private ArgumentCaptor<Double> monthlyRevenueCaptor;

    @Captor
    private ArgumentCaptor<Double> annualRevenueCaptor;


    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        createDashboardMapper = mock(CreateDashboardMapper.class);
        plansRepository=mock(PlansRepository.class);
        dashboardService = new DashboardServiceImpl(dashboardRepository,createDashboardMapper,plansRepository);

        when(plansRepository.findByName_Name("Gold")).thenReturn(Optional.of(createPlan("Gold", 100.0, 10.0)));
    }

        private Dashboard createDashboard() {

            TotalActiveSubscriptions totalActiveSubscriptions = new TotalActiveSubscriptions();
            totalActiveSubscriptions.setTotalActiveSubscriptions(100);

            TotalCanceledSubscriptions totalCanceledSubscriptions = new TotalCanceledSubscriptions();
            totalCanceledSubscriptions.setTotalCanceledSubscriptions(20);

            TotalRevenue totalRevenue = new TotalRevenue();
            totalRevenue.setTotalRevenue(1500.0);

            PlanName planName = new PlanName();
            planName.setPlanName("Gold");

            Month month = new Month();
            month.setMonth("January");

            Integer totalActiveSubscriptionsValue = getTotalActiveSubscriptionsValue(totalActiveSubscriptions);
            Integer totalCanceledSubscriptionsValue = getTotalCanceledSubscriptionsValue(totalCanceledSubscriptions);
            Double totalRevenueValue = getTotalRevenueValue(totalRevenue);
            String planNameValue = getPlanNameValue(planName);
            String monthValue = getMonthValue(month);

            return new Dashboard(totalActiveSubscriptionsValue, totalCanceledSubscriptionsValue, totalRevenueValue, planNameValue, monthValue);

        }

        private Plans createPlan(String name, Double anualFee, Double monthlyFees) {

            Name planName = new Name();
            planName.setName(name);

            Description description = new Description();
            description.setDescription("Plan Description");

            NumberOfMinutes numberOfMinutes = new NumberOfMinutes();
            numberOfMinutes.setNumberOfMinutes("100");

            MaximumNumberOfUsers maximumNumberOfUsers = new MaximumNumberOfUsers();
            maximumNumberOfUsers.setMaximumNumberOfUsers(5);

            MusicCollection musicCollection = new MusicCollection();
            musicCollection.setMusicCollection(10);

            MusicSuggestion musicSuggestion = new MusicSuggestion();
            musicSuggestion.setMusicSuggestion("personalized");

            AnnualFee annualFee = new AnnualFee();
            annualFee.setAnnualFee(anualFee);

            MonthlyFee monthlyFee = new MonthlyFee();
            monthlyFee.setMonthlyFee(monthlyFees);

            Active activeStatus = new Active();
            activeStatus.setActive(true);

            Promoted promotedStatus = new Promoted();
            promotedStatus.setPromoted(false);

            return new Plans(planName, description, numberOfMinutes, maximumNumberOfUsers,
                    musicCollection, musicSuggestion, annualFee, monthlyFee, activeStatus, promotedStatus);
        }

        private Integer getTotalActiveSubscriptionsValue(TotalActiveSubscriptions totalActiveSubscriptions) {
            return totalActiveSubscriptions.getTotalActiveSubscriptions();
        }

        private Integer getTotalCanceledSubscriptionsValue(TotalCanceledSubscriptions totalCanceledSubscriptions) {
            return totalCanceledSubscriptions.getTotalCanceledSubscriptions();
        }

        private Double getTotalRevenueValue(TotalRevenue totalRevenue) {
            return totalRevenue.getTotalRevenue();
        }

        private String getPlanNameValue(PlanName planName) {
            return planName.getPlanName();
        }

        private String getMonthValue(Month month) {
            return month.getMonth();
        }

    @Test
    void testGetRevenueTillNow() {
        Plans goldPlan = createPlan("Gold",100.00,100.00);


        when(plansRepository.findByName_Name("Gold")).thenReturn(Optional.of(goldPlan));

        List<Object[]> results = new ArrayList<>();
        Object[] row1 = {"monthly", "Gold", 2L};
        Object[] row2 = {"annually", "Gold", 3L};
        results.add(row1);
        results.add(row2);

        Dashboard result = dashboardService.getRevenueTillNow("Gold");

        verify(plansRepository).findByName_Name("Gold");

        Double expectedRevenue = (2 * 100.0) + (3 * 1000.0);
        assertEquals(expectedRevenue, result.getTotalRevenue().getTotalRevenue().doubleValue());

    }

    @Test
    public void testGetMonthlyRevenuePlan() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);

        // Set up test data
        String planName = "Gold";
        Double annualFee = 100.00;
        Double monthlyFee = 10.00;

        Plans plan = createPlan(planName, annualFee, monthlyFee);
        Integer numberMonth = 3;

        LocalDate now = LocalDate.now();
        LocalDate startDate = now.minusYears(1);
        LocalDate endDate = now;
        String startDateString = startDate.toString();
        String endDateString = endDate.toString();

        List<Object[]> results = new ArrayList<>();
        Object[] row1 = {"monthly", planName, startDateString, 2L};
        Object[] row2 = {"annually", planName, startDateString, 1L};
        results.add(row1);
        results.add(row2);

        when(plansRepository.findByName_Name(planNameCaptor.capture())).thenReturn(Optional.of(plan));

        List<DisplayRevenue> revenueList = dashboardService.getMonthlyRevenuePlan(planName, numberMonth);

        verify(plansRepository, times(2)).findByName_Name(planNameCaptor.capture());
        verifyMonthlyRevenue(planName, 2, monthlyFee * 2, 0.0, revenueList);
        verifyMonthlyRevenue(planName, 2, 0.0, annualFee, revenueList);

        Assertions.assertEquals(2, planNameCaptor.getAllValues().size());
        Assertions.assertEquals(planName, planNameCaptor.getAllValues().get(0));
        Assertions.assertEquals(planName, planNameCaptor.getAllValues().get(1));
    }

       private void verifyMonthlyRevenue(String planName, long count, double monthlyRevenue, double annualRevenue, List<DisplayRevenue> revenueList) {
        Assertions.assertEquals(1, revenueList.size());
        DisplayRevenue revenue = revenueList.get(0);
        Assertions.assertEquals(planName, revenue.getPlanName());
        List<RevenueData> revenues = revenue.getRevenues();
        Assertions.assertEquals(3, revenues.size()); // Assuming 3 months in total


    }
    }

