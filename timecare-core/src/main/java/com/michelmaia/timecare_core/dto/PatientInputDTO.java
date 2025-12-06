package com.michelmaia.timecare_core.dto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record PatientInputDTO(
        String dateOfBirth, String insuranceProvider, Long userId
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public LocalDate parsedDate() {
        return LocalDate.parse(this.dateOfBirth, FORMATTER);
    }
}
