package DashboardTest.Repository;
import com.example.psoft_22_23_project.dashboardmanagement.model.*;
import com.example.psoft_22_23_project.dashboardmanagement.repositories.DashboardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


public class DashboardRepositoryTest {
    @Mock
    private DashboardRepository dashboardRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }
    private Dashboard createDashboard(){

        TotalActiveSubscriptions totalActiveSubscriptions = new TotalActiveSubscriptions();
        totalActiveSubscriptions.setTotalActiveSubscriptions(100);

        TotalCanceledSubscriptions totalCanceledSubscriptions = new TotalCanceledSubscriptions();
        totalCanceledSubscriptions.setTotalCanceledSubscriptions(20);

        TotalRevenue totalRevenue = new TotalRevenue();
        totalRevenue.setTotalRevenue(1500.0);

        PlanName planName = new PlanName();
        planName.setPlanName("Gold");

        Month month = new Month();
        month.setMonth("June");

        Integer totalActiveSubscriptionsValue = getTotalActiveSubscriptionsValue(totalActiveSubscriptions);
        Integer totalCanceledSubscriptionsValue = getTotalCanceledSubscriptionsValue(totalCanceledSubscriptions);
        Double totalRevenueValue = getTotalRevenueValue(totalRevenue);
        String planNameValue = getPlanNameValue(planName);
        String monthValue = getMonthValue(month);

        return new Dashboard(totalActiveSubscriptionsValue, totalCanceledSubscriptionsValue, totalRevenueValue, planNameValue, monthValue);

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
    public void testNewSubscriptions(){
        String startDates=LocalDate.now().toString();
        String endDates=LocalDate.now().toString();
        Integer active = 100;
        when(dashboardRepository.findTotalActiveSubscriptions(startDates, endDates)).thenReturn(active);
        Integer total = dashboardRepository.findTotalActiveSubscriptions(startDates, endDates);
        assertEquals(active, total);
    }
    @Test
    public void testCancelledSubscriptions(){
        String startDates=LocalDate.now().toString();
        String endDates=LocalDate.now().toString();
        Integer canceled = 20;
        when(dashboardRepository.findTotalCanceledSubscriptions(startDates, endDates)).thenReturn(20);
        Integer total = dashboardRepository.findTotalCanceledSubscriptions(startDates, endDates);
        assertEquals(canceled, total);
    }

    @Test
    public void testFindPaymentTypeAndPlanNameCounts() {
        String startDate = "2023-01-01";
        String endDate = "2023-01-31";
        List<Object[]> mockedResult = new ArrayList<>();
        Object[] resultRow1 = {"monthly", "Gold", "2023-01-01", 10};
        Object[] resultRow2 = {"monthly", "Silver", "2023-01-01", 5};
        mockedResult.add(resultRow1);
        mockedResult.add(resultRow2);

        when(dashboardRepository.findPaymentTypeAndPlanNameCounts(startDate, endDate)).thenReturn(mockedResult);
        List<Object[]> result = dashboardRepository.findPaymentTypeAndPlanNameCounts(startDate, endDate);

        assertEquals(2, result.size());

        Object[] row1 = result.get(0);
        assertEquals("monthly", row1[0]);
        assertEquals("Gold", row1[1]);
        assertEquals("2023-01-01", row1[2]);
        assertEquals(10, row1[3]);

        Object[] row2 = result.get(1);
        assertEquals("monthly", row2[0]);
        assertEquals("Silver", row2[1]);
        assertEquals("2023-01-01", row2[2]);
        assertEquals(5, row2[3]);
    }

}
