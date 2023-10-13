package com.example.psoft_22_23_project.plansmanagement.model;


import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class MonthlyFee {
    @Column(name = "Monthly_Fee")
    @NotNull
    @Min(0)
    @SerializedName("monthlyFee")
    private double monthlyFee;

    public void setMonthlyFee(double monthlyFee) {
        if (monthlyFee < 0) {
            throw new IllegalArgumentException("Monthly Fee must be positive");
        }
        this.monthlyFee = monthlyFee;
    }

    public void setMonthlyFee(String monthlyFee) {
        String numericPart = monthlyFee.replaceAll("[^\\d.]", ""); // Removes non-numeric characters except '.'

        this.monthlyFee = Double.parseDouble(numericPart);
    }

}
