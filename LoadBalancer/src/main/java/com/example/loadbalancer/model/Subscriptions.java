package com.example.loadbalancer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "Subscriptions")
public class Subscriptions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String startDate;

    private String endDate;

    private String activeStatus;

    private String paymentType;

    private String plan;
    private String user;
    private Boolean isBonus;

    public Subscriptions(String paymentType, String plan, String user, Boolean isBonus) {
        this.startDate = LocalDate.now().toString();
        this.endDate = calculateEndDate(paymentType);
        this.activeStatus = "Active";
        this.paymentType = paymentType;
        this.plan = plan;
        this.user = user;
        this.isBonus = isBonus;
    }


    private String calculateEndDate(String paymentType) {
        LocalDate date = LocalDate.now();
        if ("annually".equals(paymentType)) {
            return date.plusYears(1).toString();
        } else {
            return "Not yet defined";
        }
    }
    protected  Subscriptions(){}
}
