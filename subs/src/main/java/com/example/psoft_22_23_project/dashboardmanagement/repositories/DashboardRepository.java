    package com.example.psoft_22_23_project.dashboardmanagement.repositories;
    
    import com.example.psoft_22_23_project.dashboardmanagement.model.Dashboard;
    import com.example.psoft_22_23_project.subscriptionsmanagement.model.EndDate;
    import com.example.psoft_22_23_project.subscriptionsmanagement.model.StartDate;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;
    import org.springframework.stereotype.Repository;

    import java.time.LocalDate;
    import java.util.Date;
    import java.util.List;

    @Repository
    public interface DashboardRepository extends JpaRepository<Dashboard, Long> {
    
        @Query("SELECT COUNT(s) FROM Subscriptions s WHERE s.activeStatus.active=true AND s.startDate.startDate BETWEEN :startDate AND :endDate")
        Integer findTotalActiveSubscriptions(@Param("startDate") String startDate, @Param("endDate") String endDate);
    
        @Query("SELECT COUNT(s) FROM Subscriptions s WHERE s.activeStatus.active=false AND s.startDate.startDate BETWEEN :startDate AND :endDate")
        Integer findTotalCanceledSubscriptions(@Param("startDate") String startDate, @Param("endDate") String endDate);

        @Query("SELECT s.paymentType.paymentType AS paymentType, s.plan.name.name AS planName,s.startDate.startDate AS startDate,COUNT(s) AS count " +
                "FROM Subscriptions s " +
                "WHERE s.startDate.startDate BETWEEN :startDate AND :endDate " +
                "GROUP BY s.paymentType.paymentType, s.plan.name.name,s.startDate.startDate")
        List<Object[]> findPaymentTypeAndPlanNameCounts(@Param("startDate") String startDate, @Param("endDate") String endDate);




    }
