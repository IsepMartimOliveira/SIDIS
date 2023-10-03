package com.example.psoft_22_23_project.plansmanagement.model;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Embeddable
@Data
public class MaximumNumberOfUsers {
    @Column(name = "Maximum_Number_Of_Users")
    @NotNull
    @Min(0)
    private Integer maximumNumberOfUsers;

    public void setMaximumNumberOfUsers(Integer maximumNumberOfUsers) {
        if (maximumNumberOfUsers == null || maximumNumberOfUsers < 0) {
            throw new IllegalArgumentException("Maximum Number Of Users must be positive");
        }
        this.maximumNumberOfUsers = maximumNumberOfUsers;
    }
}
