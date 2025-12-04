package com.michelmaia.timecare_notification.consumer;

import com.michelmaia.timecare_notification.dto.NotificationMessage;
import com.michelmaia.timecare_notification.model.EmailLog;
import com.michelmaia.timecare_notification.repository.EmailLogRepository;
import com.michelmaia.timecare_notification.service.EmailSenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationConsumer {
    private final EmailSenderService emailSenderService;
    private final EmailLogRepository logRepo;
    private final Logger logger = LoggerFactory.getLogger(EmailNotificationConsumer.class);

    public EmailNotificationConsumer(EmailSenderService emailSenderService,
                                     EmailLogRepository logRepo) {
        this.emailSenderService = emailSenderService;
        this.logRepo = logRepo;
    }

    @RabbitListener(queues = "notifications.queue")
    public void receive(NotificationMessage message) {

        emailSenderService.sendEmail(message.email(), message.subject(), message.body());

        EmailLog log = new EmailLog();
        log.setEmail(message.email());
        log.setSubject(message.subject());
        log.setBody(message.body());

        logRepo.save(log);

        logger.info("Email sent to: " + message.email());
    }
}
