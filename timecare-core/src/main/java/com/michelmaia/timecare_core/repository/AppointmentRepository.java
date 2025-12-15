package com.michelmaia.timecare_core.repository;

import com.michelmaia.timecare_core.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByDateTimeAfter(LocalDateTime dateTime);
    List<Appointment> findByPatientIdAndDateTimeAfter(Long patientId, LocalDateTime dateTime);
    List<Appointment> findByPatientId(Long patientId);
}
