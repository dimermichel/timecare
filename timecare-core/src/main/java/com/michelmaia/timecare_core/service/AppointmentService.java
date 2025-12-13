package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.dto.AppointmentInputDTO;
import com.michelmaia.timecare_core.exception.AccessDeniedGraphQLException;
import com.michelmaia.timecare_core.messaging.NotificationProducer;
import com.michelmaia.timecare_core.model.Appointment;
import com.michelmaia.timecare_core.model.AppointmentStatus;
import com.michelmaia.timecare_core.model.Medic;
import com.michelmaia.timecare_core.model.Patient;
import com.michelmaia.timecare_core.repository.AppointmentRepository;
import com.michelmaia.timecare_core.repository.MedicRepository;
import com.michelmaia.timecare_core.repository.PatientRepository;
import com.michelmaia.timecare_core.repository.UserRepository;
import com.michelmaia.timecare_core.security.CurrentUser;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepo;
    private final CurrentUser currentUser;
    private final UserRepository userRepo;
    private final MedicRepository medicRepo;
    private final PatientRepository patientRepo;
    private final NotificationProducer producer;
    private final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    public List<Appointment> getAllAppointments() {
        if (isMedicalStaff()) {
            return appointmentRepo.findAll();
        }
        if (isPatient()) {
            return appointmentRepo.findByPatientId(getCurrentUserPatientId());
        }
        throw new AccessDeniedGraphQLException("Unauthorized role");
    }

    public List<Appointment> getAllAppointmentsByPatient(Long id) {
        if (isMedicalStaff()) {
            return appointmentRepo.findByPatientId(id);
        }
        checkPatientAccess(id);
        return appointmentRepo.findByPatientId(id);
    }

    public Appointment getAppointmentById(Long id) {
        Appointment ap = appointmentRepo.findById(id)
                .orElseThrow(() -> new AccessDeniedGraphQLException("Appointment not found"));

        if (isMedicalStaff()) {
            return ap;
        }

        checkPatientAccess(ap.getPatient().getId());
        return ap;
    }

    public Appointment create(AppointmentInputDTO input) {
        validateInput(input);

        Medic medic = findMedicOrThrow(input.medicId());
        Patient patient = findPatientOrThrow(input.patientId());

        if(isPatient()) {
            // Avoid Patient scheduling Medical Appointments to different Patients
            checkPatientAccess(patient.getId());
        }

        Appointment appointment = new Appointment();
        appointment.setDateTime(input.parsedDateTime());
        appointment.setMedic(medic);
        appointment.setPatient(patient);

        return saveAndNotify(appointment, "Medical Appointment Scheduled",  input.parsedDateTime().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")));
    }

    public Appointment update(Long id, AppointmentInputDTO input) {
        validateInput(input);

        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));

        Medic medic = findMedicOrThrow(input.medicId());
        Patient patient = findPatientOrThrow(input.patientId());

        if(isPatient()) {
            // Avoid Patient scheduling Medical Appointments to different Patients
            checkPatientAccess(patient.getId());
        }

        appointment.setDateTime(input.parsedDateTime());
        appointment.setMedic(medic);
        appointment.setPatient(patient);

        return saveAndNotify(appointment, "Updated Medical Appointment Scheduled", input.parsedDateTime().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")));
    }

    public Appointment updateStatus(Long id, AppointmentStatus status) {
        Appointment appointment = appointmentRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found with id: " + id));

        appointment.setStatus(status);

        if(isMedicalStaff()) {
            return saveAndNotify(appointment, "Medical Appointment Status Updated - " + status, appointment.getDateTime().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")));
        }

        // Patients can only update their own appointment status
        checkPatientAccess(appointment.getPatient().getId());
        return saveAndNotify(appointment, "Medical Appointment Status Updated - " + status, appointment.getDateTime().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy 'at' hh:mm a")));

    }

    private boolean isMedicalStaff() {
        return currentUser.hasRole("MEDIC") || currentUser.hasRole("NURSE");
    }

    private boolean isPatient() {
        return currentUser.hasRole("PATIENT");
    }

    private Long getCurrentUserPatientId() {
        return userRepo.findByEmail(currentUser.getUsername())
                .map(user -> user.getId())
                .orElseThrow(() -> new AccessDeniedGraphQLException("Current user not found"));
    }

    private void checkPatientAccess(Long targetPatientId) {
        if (isPatient()) {
            if (!getCurrentUserPatientId().equals(targetPatientId)) {
                throw new AccessDeniedGraphQLException("Patients may only access their own appointments");
            }
            return;
        }
        throw new AccessDeniedGraphQLException("Unauthorized role");
    }

    private Medic findMedicOrThrow(Long id) {
        return medicRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found"));
    }

    private Patient findPatientOrThrow(Long id) {
        return patientRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Patient not found"));
    }

    private void validateInput(AppointmentInputDTO input) {
        if (input == null) throw new IllegalArgumentException("Appointment input cannot be null");
        if (input.dateTime() == null || input.dateTime().isBlank())
            throw new IllegalArgumentException("DateTime is required");
        if (input.medicId() == null) throw new IllegalArgumentException("Medic ID is required");
        if (input.patientId() == null) throw new IllegalArgumentException("Patient ID is required");
    }

    private Appointment saveAndNotify(Appointment appointment, String subject, String dateString) {
        Appointment saved = appointmentRepo.save(appointment);
        try {
            String patientName = saved.getPatient().getUser().getName();
            String medicName = saved.getMedic().getUser().getName();
            String message = "Hello " + patientName + ", your appointment with Dr. " + medicName
                    + " is scheduled for " + dateString;

            producer.sendNotification(saved.getPatient().getUser().getEmail(), subject, message);
        } catch (Exception e) {
            logger.warn("Notification delivery failed for appointment ID {}, but appointment was saved successfully. Error: {}",
                    saved.getId(), e.getMessage());
        }
        return saved;
    }
}
