package com.michelmaia.timecare_notification.dto;

public record NotificationMessage(
        String email, String subject, String body
) {
}
