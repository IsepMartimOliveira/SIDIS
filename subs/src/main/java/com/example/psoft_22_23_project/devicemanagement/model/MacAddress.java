package com.example.psoft_22_23_project.devicemanagement.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Embeddable
public class MacAddress {

    @Column(name = "macAddress", nullable = false)
    @NotNull
    @Size(min = 17, max = 17)
    private String macAddress;

    public void setMacAddress(String macAddress) {

        if (macAddress == null || macAddress.isBlank()) {
            throw new IllegalArgumentException("'macAddress' is a mandatory attribute of Device");
        }
        if (!macAddress.matches("^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})$")) {
            throw new IllegalArgumentException("Invalid character(s) in 'macAddress', enter a valid Mac Address");
        }
        this.macAddress = macAddress;

    }

    public MacAddress() {
    }
}
