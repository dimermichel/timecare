package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.model.Patient;
import com.michelmaia.timecare_core.repository.PatientRepository;
import com.michelmaia.timecare_core.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PatientService {
    private final PatientRepository patientRepo;
    private final UserRepository userRepo;

    public PatientService(PatientRepository patientRepo, UserRepository userRepo) {
        this.patientRepo = patientRepo;
        this.userRepo = userRepo;
    }

    public List<Patient> getAllPatients() {
        return patientRepo.findAll();
    }

    public Optional<Patient> getPatientById(Long id) {
        return patientRepo.findById(id);
    }

   public Patient create(Patient patient) {
       return patientRepo.save(patient);
   }
}
