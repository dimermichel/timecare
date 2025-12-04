package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.dto.DoctorInputDTO;
import com.michelmaia.timecare_core.entity.Doctor;
import com.michelmaia.timecare_core.service.DoctorService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class DoctorGraphQLController {
    private final DoctorService doctorService;

    public DoctorGraphQLController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @QueryMapping
    public List<Doctor> doctors() {
        return doctorService.getAllDoctors();
    }

    @QueryMapping
    public Optional<Doctor> doctorById(@Argument Long id) {
        return doctorService.getDoctorById(id);
    }

    @MutationMapping
    public Doctor createDoctor(@Argument(name = "input") DoctorInputDTO doctorInput) {
        return doctorService.create(doctorInput);
    }

}
