package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.model.Nurse;
import com.michelmaia.timecare_core.service.NurseService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class NurseGraphQLController {
    private final NurseService nurseService;

    public NurseGraphQLController(NurseService nurseService) {
        this.nurseService = nurseService;
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Nurse> nurses() {
        return nurseService.getAllNurses();
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Optional<Nurse> nurseById(@Argument Long id) {
        return nurseService.getNurseById(id);
    }

}
