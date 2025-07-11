package com.example.psoft_22_23_project.plansmanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EditPlanRequestUpdate {
    private String name;
    private EditPlansRequest editPlansRequest;
    private long desiredVersion;
}
