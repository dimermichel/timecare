package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.dto.AppointmentInputDTO;
import com.michelmaia.timecare_core.entity.Appointment;
import com.michelmaia.timecare_core.service.AppointmentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;

    public AppointmentGraphQLController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Queries
    @QueryMapping
    public List<Appointment> appointments() {
        return appointmentService.getAllAppointments();
    }

    @QueryMapping
    public Appointment appointmentById(@Argument Long id) {
        return appointmentService.getAppointmentById(id).orElseThrow();
    }

    @QueryMapping
    public List<Appointment> appointmentsByPatient(@Argument Long patientId) {
        return appointmentService.getAllAppointmentsByPatient(patientId);
    }

    // Mutations
    @MutationMapping
    public Appointment createAppointment(@Argument(name = "input") AppointmentInputDTO appointmentInput) {
        return appointmentService.create(appointmentInput);
    }

}
