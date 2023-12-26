package com.example.psoft_22_23_project.subscriptionsmanagement.model;



//import com.example.psoft_22_23_project.plansmanagement.model.Plans;
//import com.example.psoft_22_23_project.usermanagement.model.User;

import lombok.Getter;
import org.hibernate.StaleObjectStateException;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;


@Entity
@Getter
@Table(name = "Subscriptions")
public class Subscriptions {
    protected Subscriptions(){

    }

    public Subscriptions(String plan, PaymentType paymentType, String user,Boolean isBonus) {
        this.startDate = new StartDate();
        this.endDate = new EndDate(paymentType.getPaymentType());
        this.activeStatus = new ActiveStatus(true);
        this.plan = plan;
        this.paymentType = paymentType;
        this.user = user;
        this.isBonus=isBonus;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    private long version;

    @Embedded
    private StartDate startDate;

    @Embedded
    private EndDate endDate;

    @Embedded
    private ActiveStatus activeStatus;

    @Embedded
    private PaymentType paymentType;

    private String plan;
    private String user;
    private Boolean isBonus;
    public void deactivate(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        // apply patch only if the field was sent in the request. we do not allow to
        // change the name attribute so we simply ignore it
        this.activeStatus.setActive(false);
    }


    public void checkChange(final long desiredVersion) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
    }

    public void changePlan(final long desiredVersion, final String plan) {
        // check current version
        if (this.version != desiredVersion) {
            throw new StaleObjectStateException("Object was already modified by another user", this.id);
        }
        if (Objects.equals(this.plan, plan))
        {
            throw new IllegalArgumentException("Can not change to same plan");

        }
        this.plan = plan;


    }

    public void cancelSubscription(final long desiredVersion) {
        deactivate(desiredVersion);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate startDate = LocalDate.parse(getStartDate().getStartDate(), formatter);

        if (Objects.equals(getPaymentType().getPaymentType(), "monthly")) {
            if (startDate.getMonthValue() == LocalDate.now().getMonthValue()) {
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                } else {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            } else if (startDate.getDayOfMonth() >= LocalDate.now().getDayOfMonth()) {
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                } else {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths(1)));
                }

            } else {
                if (startDate.getYear() != LocalDate.now().getYear()) {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1).plusYears(LocalDate.now().getYear() - startDate.getYear())));
                } else {
                    getEndDate().setEndDate(String.valueOf(startDate.plusMonths((LocalDate.now().getMonthValue() - startDate.getMonthValue()) + 1)));
                }
            }
        }
    }

    public void renewSub(long desiredVersion) {
        if (Objects.equals(getPaymentType().getPaymentType(), "monthly")) {

            throw new IllegalArgumentException("You can not renew a monthly subscription");
        } else {
            checkChange(desiredVersion);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDate = LocalDate.parse(getEndDate().getEndDate(), formatter);
            getEndDate().setEndDate(String.valueOf(endDate.plusYears(1)));
        }
    }

    public void changeBonus(Boolean isBonus,String plan) {
        this.isBonus=isBonus;
        this.plan=plan;
    }
}
