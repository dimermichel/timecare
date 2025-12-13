package com.michelmaia.timecare_notification.listener;

import com.michelmaia.timecare_notification.service.FailedNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class DLQNotificationListener {
    private final FailedNotificationService failedService;

    public DLQNotificationListener(FailedNotificationService failedService) {
        this.failedService = failedService;
    }

    @RabbitListener(queues = "email.notifications.dlq")
    public void receiveDeadLetter(Map<String, Object> message) {
        log.error("📨 Message arrived in DLQ: {}", message);

        // Save to Mongo for manual inspection
        failedService.save(message, "Message moved to DLQ (possible processing failure)");

    }
}
