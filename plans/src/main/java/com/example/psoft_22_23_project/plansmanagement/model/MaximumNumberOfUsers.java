package com.example.psoft_22_23_project.plansmanagement.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

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
    @SerializedName("maximumNumberOfUsers")
    private Integer maximumNumberOfUsers;

    public void setMaximumNumberOfUsers(String maximumNumberOfUsers) {
        this.maximumNumberOfUsers = Integer.valueOf(maximumNumberOfUsers);
    }

    public void setMaximumNumberOfUsers(Integer maximumNumberOfUsers) {
        if (maximumNumberOfUsers == null || maximumNumberOfUsers < 0) {
            throw new IllegalArgumentException("Maximum Number Of Users must be positive");
        }
        this.maximumNumberOfUsers = maximumNumberOfUsers;
    }

}
