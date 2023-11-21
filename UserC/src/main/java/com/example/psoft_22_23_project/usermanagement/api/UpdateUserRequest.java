package com.example.psoft_22_23_project.usermanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@AllArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    String fullName;

    Set<String> authorities;
}
