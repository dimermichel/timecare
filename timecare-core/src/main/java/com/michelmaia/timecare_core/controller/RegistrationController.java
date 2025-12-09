package com.michelmaia.timecare_core.controller;

import com.michelmaia.timecare_core.dto.RegistrationDTO;
import com.michelmaia.timecare_core.service.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/register")
public class RegistrationController {
    private final RegistrationService registrationService;
    private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping
    public void registerUser(@RequestBody RegistrationDTO input) {
        logger.info("Registering user: {}", input);
        registrationService.register(input);
    }

}
