package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.model.Patient;
import com.michelmaia.timecare_core.service.PatientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientGraphQLController {
    private final PatientService patientService;

    public PatientGraphQLController(PatientService patientService) {
        this.patientService = patientService;
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Patient> patients() {
        return patientService.getAllPatients();
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Optional<Patient> patientById(@Argument Long id) {
        return patientService.getPatientById(id);
    }

}
