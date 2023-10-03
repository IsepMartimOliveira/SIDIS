package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Data;
import lombok.Getter;

import javax.persistence.Embeddable;
import javax.validation.constraints.Pattern;
import java.util.Date;

@Getter
@Data
@Embeddable
public class PaymentType {


    private String paymentType;

    public PaymentType(String paymentType ) {
        this.paymentType = paymentType;
    }

    public PaymentType() {

    }
}
