package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.dto.AppointmentInputDTO;
import com.michelmaia.timecare_core.model.Appointment;
import com.michelmaia.timecare_core.model.Medic;
import com.michelmaia.timecare_core.model.Patient;
import com.michelmaia.timecare_core.messaging.NotificationProducer;
import com.michelmaia.timecare_core.repository.AppointmentRepository;
import com.michelmaia.timecare_core.repository.MedicRepository;
import com.michelmaia.timecare_core.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepo;
    private final MedicRepository medicRepo;
    private final PatientRepository patientRepo;
    private final NotificationProducer producer;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    public AppointmentService(AppointmentRepository appointmentRepo, MedicRepository medicRepo, PatientRepository patientRepo, NotificationProducer producer) {
        this.appointmentRepo = appointmentRepo;
        this.medicRepo = medicRepo;
        this.patientRepo = patientRepo;
        this.producer = producer;
    }

    public List<Appointment> getAllAppointments() {
        return appointmentRepo.findAll();
    }

    public List<Appointment> getAllAppointmentsByPatient(Long id) {
        return appointmentRepo.findByPatientId(id);
    }

    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepo.findById(id);
    }

    public Appointment create(AppointmentInputDTO appointmentInput) {

        if (appointmentInput == null) {
            throw new IllegalArgumentException("Appointment input cannot be null");
        }

        if (appointmentInput.dateTime() == null || appointmentInput.dateTime().isBlank()) {
            throw new IllegalArgumentException("DateTime is required");
        }

        if (appointmentInput.doctorId() == null) {
            throw new IllegalArgumentException("Doctor ID is required");
        }

        if (appointmentInput.patientId() == null) {
            throw new IllegalArgumentException("Patient ID is required");
        }

        Medic medic = medicRepo.findById(appointmentInput.doctorId()).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
        Patient patient = patientRepo.findById(appointmentInput.patientId()).orElseThrow(() -> new IllegalArgumentException("Patient not found"));

        Appointment appointment = new Appointment();
        appointment.setDateTime(appointmentInput.parsedDateTime());
        appointment.setMedic(medic);
        appointment.setPatient(patient);

        Appointment saved = appointmentRepo.save(appointment);

        // Send notification asynchronously
        try {
            producer.sendNotification(
                    patient.getUser().getEmail(),
                    "Medical Appointment Scheduled",
                    "Hello " + patient.getUser().getName() + ", your appointment with Dr. " + medic.getUser().getName()
                            + " is scheduled for " + appointmentInput.dateTime()
            );
        } catch (Exception e) {
            logger.warn("Notification delivery failed for appointment ID {}, but appointment was created successfully. Error: {}",
                    saved.getId(), e.getMessage());
        }

        return saved;
    }

}
