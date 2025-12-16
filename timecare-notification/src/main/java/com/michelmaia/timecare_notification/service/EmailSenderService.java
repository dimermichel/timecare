package com.michelmaia.timecare_notification.service;

import com.michelmaia.timecare_notification.model.EmailLog;
import com.michelmaia.timecare_notification.model.FailedNotification;
import com.michelmaia.timecare_notification.repository.EmailLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class EmailSenderService {
    private final EmailLogRepository logRepo;
    private final RabbitTemplate rabbitTemplate;
    private static int emailCounter = 0; // Track email count for simulation

    public EmailSenderService(EmailLogRepository logRepo, RabbitTemplate rabbitTemplate) {
        this.logRepo = logRepo;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendEmail(String to, String subject, String body) {
        emailCounter++;

        try {
            // TODO integrate SMTP (Gmail, SES, etc) - simulate email sending
            // Simulate failure every 5th email (like real SMTP failures)
            if (emailCounter % 5 == 0) {
                log.error("⚠️ SMTP connection failed for email #{} to: {}", emailCounter, to);
                throw new RuntimeException("SMTP server connection timeout - could not send email to: " + to);
            }

            log.info("✅ Simulating sending email to: {}\nSubject: {}\nBody: {}", to, subject, body);
            EmailLog emailLog = new EmailLog();
            emailLog.setEmail(to);
            emailLog.setSubject(subject);
            emailLog.setBody(body);
            logRepo.save(emailLog);
            log.info("Email logged successfully to: {}", to);
        } catch (Exception e) {
            log.error("❌ Failed to send email to: {}. Sending to DLQ. Error: {}", to, e.getMessage());
            FailedNotification failedNotification = new FailedNotification(
                    Map.of("email", to, "subject", subject, "body", body),
                    "Failed to log email after sending"
            );
            rabbitTemplate.convertAndSend("email.notifications.dlq", failedNotification.getPayload());
        }
    }
}
