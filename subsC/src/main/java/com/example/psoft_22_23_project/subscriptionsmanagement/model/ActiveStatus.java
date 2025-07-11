package com.example.psoft_22_23_project.subscriptionsmanagement.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.Embeddable;

@Getter
@Setter
@Embeddable
public class ActiveStatus {
    @NonNull
    private boolean active;
    public ActiveStatus(boolean active) {
        this.active = active;
    }
    public ActiveStatus() {
        this.active = true;
    }
}

