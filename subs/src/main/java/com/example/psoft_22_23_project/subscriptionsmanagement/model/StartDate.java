package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Getter;

import javax.persistence.Embeddable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
@Getter
@Embeddable
public class StartDate {

    private String startDate;

    public StartDate() {

        this.startDate = String.valueOf(LocalDate.now());
    }
}
