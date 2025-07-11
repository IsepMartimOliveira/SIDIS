package com.example.loadbalancer.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPlanRequestUpdate {
    private String name;
    private EditPlansRequest editPlansRequest;
    private long desiredVersion;
}
