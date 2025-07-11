package com.example.psoft_22_23_project.plansmanagement.api;

import com.example.psoft_22_23_project.plansmanagement.model.*;
import lombok.Data;

import javax.persistence.*;

@Data
public class PlansBonus {

    private String username;
    private String name;
    private String description;
    private String numberOfMinutes;
    private String maximumNumberOfUsers;
    private String musicCollection;
    private String musicSuggestion;
    private String annualFee;
    private String monthlyFee ;
    private String active;
    private String promoted;
    private String deleted;
}
