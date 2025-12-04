package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.dto.PatientInputDTO;
import com.michelmaia.timecare_core.entity.Patient;
import com.michelmaia.timecare_core.repository.PatientRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepo;

    public PatientService(PatientRepository patientRepo) {
        this.patientRepo = patientRepo;
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepo.findById(id);
    }

    public Patient create(PatientInputDTO patientInput) {
        if (patientInput == null) {
            throw new IllegalArgumentException("Patient input cannot be null");
        }
        
        if (patientInput.name() == null || patientInput.name().isBlank()) {
            throw new IllegalArgumentException("Patient name is required and cannot be blank");
        }
        
        if (patientInput.email() == null || patientInput.email().isBlank()) {
            throw new IllegalArgumentException("Patient email is required and cannot be blank");
        }
        
        Patient patient = new Patient();
        patient.setName(patientInput.name().trim());
        patient.setEmail(patientInput.email().trim());
        return patientRepo.save(patient);
    }
}
