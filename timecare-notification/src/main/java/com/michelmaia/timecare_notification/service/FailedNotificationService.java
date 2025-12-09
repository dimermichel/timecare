package com.michelmaia.timecare_notification.service;

import com.michelmaia.timecare_notification.model.FailedNotification;
import com.michelmaia.timecare_notification.repository.FailedNotificationRepository;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FailedNotificationService {

    private final FailedNotificationRepository repo;

    public FailedNotificationService(FailedNotificationRepository repo) {
        this.repo = repo;
    }

    public FailedNotification save(Map<String, Object> payload, String reason) {
        FailedNotification fn = new FailedNotification(payload, reason);
        return repo.save(fn);
    }
}
