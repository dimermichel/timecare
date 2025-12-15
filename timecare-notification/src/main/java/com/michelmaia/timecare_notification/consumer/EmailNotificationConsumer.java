package com.michelmaia.timecare_notification.consumer;

import com.michelmaia.timecare_notification.dto.NotificationMessage;
import com.michelmaia.timecare_notification.service.EmailSenderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailNotificationConsumer {
    private final EmailSenderService emailSenderService;

    public EmailNotificationConsumer(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @RabbitListener(queues = "email.notifications.queue")
    public void receive(NotificationMessage message) {
        log.info("📨 Message arrived in email.notifications.queue: {}", message);
        emailSenderService.sendEmail(message.email(), message.subject(), message.body());
    }
}
