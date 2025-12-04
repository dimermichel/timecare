package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.dto.PatientInputDTO;
import com.michelmaia.timecare_core.entity.Patient;
import com.michelmaia.timecare_core.service.PatientService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class PatientGraphQLController {
    private final PatientService patientService;

    public PatientGraphQLController(PatientService patientService) {
        this.patientService = patientService;
    }

    @QueryMapping
    public List<Patient> patients() {
        return patientService.getAllPatients();
    }

    @QueryMapping
    public Optional<Patient> patientById(@Argument Long id) {
        return patientService.getPatientById(id);
    }

    @MutationMapping
    public Patient createPatient(@Argument(name = "input") PatientInputDTO patientInput) {
        return patientService.create(patientInput);
    }
}
