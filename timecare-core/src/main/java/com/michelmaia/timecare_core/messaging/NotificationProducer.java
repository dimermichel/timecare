package com.michelmaia.timecare_core.messaging;

import com.michelmaia.timecare_core.config.RabbitMQConfig;
import com.michelmaia.timecare_core.messaging.dto.NotificationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationProducer {
    private final RabbitTemplate rabbitTemplate;
    private final Logger logger = LoggerFactory.getLogger(NotificationProducer.class);

    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendNotification(String email, String subject, String body) {
        try {
            NotificationMessage message = new NotificationMessage(email, subject, body);
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.NOTIFICATION_EXCHANGE,
                    "notification.email",
                    message
            );
            logger.info("Notification sent to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send notification to {} with subject '{}'. Error: {}", 
                    email, subject, e.getMessage(), e);
        }
    }
}
