package com.michelmaia.timecare_notification.service;

import org.springframework.stereotype.Service;

@Service
public class EmailSenderService {
    public void sendEmail(String to, String subject, String body) {
        // TODO integrate SMTP (Gmail, SES, etc)
        System.out.println("Sending email to: " + to +
                "\nSubject: " + subject + "\nBody: " + body);
    }
}
