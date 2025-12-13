package com.michelmaia.timecare_core.messaging.dto;

public record NotificationMessage(
        String email, String subject, String body
) {}