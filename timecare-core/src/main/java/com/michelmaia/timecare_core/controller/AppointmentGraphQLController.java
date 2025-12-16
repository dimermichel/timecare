package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.dto.AppointmentInputDTO;
import com.michelmaia.timecare_core.model.Appointment;
import com.michelmaia.timecare_core.model.AppointmentStatus;
import com.michelmaia.timecare_core.service.AppointmentService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AppointmentGraphQLController {

    private final AppointmentService appointmentService;

    public AppointmentGraphQLController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    // Queries
    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Appointment> appointments(@Argument Boolean justUpcoming) {
        Boolean filterUpcoming = justUpcoming != null && justUpcoming;
        return appointmentService.getAllAppointments(filterUpcoming);
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public Appointment appointmentById(@Argument Long id) {
        return appointmentService.getAppointmentById(id);
    }

    @PreAuthorize("isAuthenticated()")
    @QueryMapping
    public List<Appointment> appointmentsByPatient(@Argument Long patientId) {
        return appointmentService.getAllAppointmentsByPatient(patientId);
    }

    // Mutations
    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Appointment createAppointment(@Argument(name = "input") AppointmentInputDTO appointmentInput) {
        return appointmentService.create(appointmentInput);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Appointment updateAppointment(@Argument Long id, @Argument(name = "input") AppointmentInputDTO appointmentInput) {
        return appointmentService.update(id, appointmentInput);
    }

    @PreAuthorize("isAuthenticated()")
    @MutationMapping
    public Appointment updateAppointmentStatus(@Argument Long id, @Argument AppointmentStatus status) {
        return appointmentService.updateStatus(id, status);
    }

}
