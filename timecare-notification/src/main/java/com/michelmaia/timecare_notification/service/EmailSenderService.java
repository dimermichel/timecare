package com.michelmaia.timecare_notification.service;

import com.michelmaia.timecare_notification.model.EmailLog;
import com.michelmaia.timecare_notification.repository.EmailLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailSenderService {
    private final EmailLogRepository logRepo;
    private static int emailCounter = 0; // Track email count for simulation

    public EmailSenderService(EmailLogRepository logRepo) {
        this.logRepo = logRepo;
    }

    public void sendEmail(String to, String subject, String body) {
        emailCounter++;

        // Simulate failure every 5th email (like real SMTP failures)
        if (emailCounter % 5 == 0) {
            log.error("⚠️ SMTP connection failed for email #{} to: {}", emailCounter, to);
            throw new RuntimeException("SMTP server connection timeout - could not send email to: " + to);
        }

        // TODO integrate SMTP (Gmail, SES, etc) - simulate email sending
        log.info("✅ Simulating sending email to: {}\nSubject: {}\nBody: {}", to, subject, body);

        // Only logged successful emails
        try {
            EmailLog emailLog = new EmailLog();
            emailLog.setEmail(to);
            emailLog.setSubject(subject);
            emailLog.setBody(body);
            logRepo.save(emailLog);
            log.info("Email logged successfully to: {}", to);
        } catch (Exception e) {
            log.warn("Email sent but failed to log for: {}. Error: {}", to, e.getMessage());
        }
    }
}
