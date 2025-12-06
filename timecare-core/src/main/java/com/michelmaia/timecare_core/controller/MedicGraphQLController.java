package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.model.Medic;
import com.michelmaia.timecare_core.service.MedicService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class MedicGraphQLController {
    private final MedicService medicService;

    public MedicGraphQLController(MedicService medicService) {
        this.medicService = medicService;
    }

    @QueryMapping
    public List<Medic> medics() {
        return medicService.getAllDoctors();
    }

    @QueryMapping
    public Optional<Medic> medicById(@Argument Long id) {
        return medicService.getDoctorById(id);
    }

}
