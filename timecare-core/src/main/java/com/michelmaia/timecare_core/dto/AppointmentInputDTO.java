package com.michelmaia.timecare_core.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record AppointmentInputDTO(
        String date, String dateTime, Long medicId, Long patientId
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    public LocalDateTime parsedDateTime() {
        return LocalDateTime.parse(this.dateTime, FORMATTER);
    }
}
