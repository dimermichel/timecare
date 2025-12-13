package com.michelmaia.timecare_core.bootstrap;

import com.michelmaia.timecare_core.dto.RegistrationDTO;
import com.michelmaia.timecare_core.model.Role;
import com.michelmaia.timecare_core.model.User;
import com.michelmaia.timecare_core.service.RegistrationService;
import com.michelmaia.timecare_core.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RegistrationService registrationService;
    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    public DataInitializer(RegistrationService registrationService, UserService userService) {
        this.registrationService = registrationService;
        this.userService = userService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<User> userList = userService.getAllUsers();
        if (userList.isEmpty()) {
            // Create default medic user
            RegistrationDTO medic = new RegistrationDTO();
            medic.setEmail("medic@email.com");
            medic.setPassword("medic");
            medic.setName("Medic");
            medic.setRole(Role.MEDIC);
            medic.setSpecialty("Cardiologist");
            registrationService.register(medic);

            //Create the default patient user
            RegistrationDTO patient = new RegistrationDTO();
            patient.setEmail("patient@email.com");
            patient.setPassword("patient");
            patient.setName("Patient");
            patient.setRole(Role.PATIENT);
            patient.setDateOfBirth(LocalDate.of(2020, 1, 1));
            patient.setInsuranceProvider("Insurance Provider");
            registrationService.register(patient);

            //Create the default nurse user
            RegistrationDTO nurse = new RegistrationDTO();
            nurse.setEmail("nurse@email.com");
            nurse.setPassword("nurse");
            nurse.setName("Nurse");
            nurse.setRole(Role.NURSE);
            nurse.setDepartment("Department");
            registrationService.register(nurse);

            logger.info("Default users created.");
        }
    }
}
