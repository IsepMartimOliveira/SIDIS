package DashboardTest.Model;

import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DashboardTest {

    @Test
    public void testDashboardCreation() {
        Integer totalActiveSubscriptions = 100;
        Integer totalCanceledSubscriptions = 20;
        Double totalRevenue = 1500.0;
        String planName = "Gold";
        String month = "January";

        Dashboard dashboard = new Dashboard(totalActiveSubscriptions, totalCanceledSubscriptions, totalRevenue, planName, month);

        Assertions.assertEquals(totalActiveSubscriptions, dashboard.getTotalActiveSubscriptions().getTotalActiveSubscriptions());
        Assertions.assertEquals(totalCanceledSubscriptions, dashboard.getTotalCanceledSubscriptions().getTotalCanceledSubscriptions());
        Assertions.assertEquals(totalRevenue, dashboard.getTotalRevenue().getTotalRevenue());
        Assertions.assertEquals(planName, dashboard.getPlanName().getPlanName());
        Assertions.assertEquals(month, dashboard.getMonth().getMonth());
    }
}