package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@Data
public class NumberOfMinutes {
    @Column(name = "Number_Of_Minutes")
    @NotNull
    @Size(min = 1)
    private String numberOfMinutes;

    public void setNumberOfMinutes(String numberOfMinutes) {
        if (numberOfMinutes == null || numberOfMinutes.isBlank()) {
            throw new IllegalArgumentException("Number Of Minutes is a mandatory attribute of Plan");
        }
        if (!isValidInput(numberOfMinutes)){
            throw new IllegalArgumentException("Number Of Minutes can only be a positive number or 'unlimited'");
        }
        this.numberOfMinutes = numberOfMinutes;
    }

    public static boolean isValidInput(String input) {
        // Regex pattern for matching "unlimited" or a positive number
        String pattern = "^unlimited$|^[1-9]\\d*$";

        // Create a pattern object
        Pattern regex = Pattern.compile(pattern);

        // Create a matcher object
        Matcher matcher = regex.matcher(input);

        // Check if the input matches the pattern
        return matcher.matches();
    }
}
