package com.michelmaia.timecare_core.dto;

import com.michelmaia.timecare_core.model.Role;
import lombok.Data;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Data
public class RegistrationDTO {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

    // Patient specific fields
    private LocalDate dateOfBirth;
    private String insuranceProvider;

    // Medic specific fields
    private String specialty;

    // Nurse specific fields
    private String department;
}
