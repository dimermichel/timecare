package com.michelmaia.timecare_core.service;

import com.michelmaia.timecare_core.dto.RegistrationDTO;
import com.michelmaia.timecare_core.model.Medic;
import com.michelmaia.timecare_core.model.Nurse;
import com.michelmaia.timecare_core.model.Patient;
import com.michelmaia.timecare_core.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final UserService userService;
    private final MedicService medicService;
    private final PatientService patientService;
    private final NurseService nurseService;

    public RegistrationService(UserService userService, MedicService medicService, PatientService patientService, NurseService nurseService) {
        this.userService = userService;
        this.medicService = medicService;
        this.patientService = patientService;
        this.nurseService = nurseService;
    }

    @Transactional
    public User register(RegistrationDTO request) {


        User user = new User();
        user.setEmail(request.getEmail());
        user.setName(request.getName());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User createdUser = userService.createUser(user);

        switch (request.getRole()) {
            case PATIENT -> createPatientProfile(createdUser, request);
            case MEDIC -> createMedicProfile(createdUser, request);
            case NURSE -> createNurseProfile(createdUser, request);
            default -> throw new IllegalStateException("Unsupported role: " + request.getRole());
        }

        return createdUser;
    }

    private void createPatientProfile(User user, RegistrationDTO request) {
        if (request.getDateOfBirth() == null) {
            throw new IllegalStateException("Patient registration requires Date of Birth.");
        }

        Patient patient = new Patient();
        patient.setDateOfBirth(request.getDateOfBirth());
        patient.setInsuranceProvider(request.getInsuranceProvider());
        patient.setUser(user); // Link the profile to the created user

        patientService.create(patient);
    }

    private void createMedicProfile(User user, RegistrationDTO request) {
        if (request.getSpecialty() == null || request.getSpecialty().isBlank()) {
            throw new IllegalStateException("Medic registration requires a specialty.");
        }

        Medic medic = new Medic();
        medic.setSpecialty(request.getSpecialty());
        medic.setUser(user);

        medicService.create(medic);
    }

    private void createNurseProfile(User user, RegistrationDTO request) {
        if (request.getDepartment() == null || request.getDepartment().isBlank()) {
            throw new IllegalStateException("Nurse registration requires a department.");
        }

        Nurse nurse = new Nurse();
        nurse.setDepartment(request.getDepartment());
        nurse.setUser(user);

        nurseService.create(nurse);
    }
}
