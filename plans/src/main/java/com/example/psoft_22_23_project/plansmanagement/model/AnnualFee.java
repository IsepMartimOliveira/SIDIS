package com.example.psoft_22_23_project.plansmanagement.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class AnnualFee {
    @Column(name = "Annual_Fee")
    @NotNull
    @SerializedName("annualFee")
    @Min(0)
    private double annualFee;

    public void setAnnualFee(double annualFee) {
        if (annualFee < 0) {
            throw new IllegalArgumentException("Annual_Fee must be positive");
        }
        this.annualFee = annualFee;
    }

    public void setAnnualFee(String annualFee) {
        String numericPart = annualFee.replaceAll("[^\\d.]", ""); // Removes non-numeric characters except '.'

        this.annualFee = Double.parseDouble(numericPart);
    }
}
