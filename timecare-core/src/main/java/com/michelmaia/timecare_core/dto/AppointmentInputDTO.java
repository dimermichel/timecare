package com.michelmaia.timecare_core.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AppointmentInputDTO(
        String date, String dateTime, Long doctorId, Long patientId
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    public LocalDateTime parsedDateTime() {
        return LocalDateTime.parse(this.dateTime, FORMATTER);
    }
}
