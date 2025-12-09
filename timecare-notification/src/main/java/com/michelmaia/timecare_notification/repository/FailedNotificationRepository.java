package com.michelmaia.timecare_notification.repository;

import com.michelmaia.timecare_notification.model.FailedNotification;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FailedNotificationRepository extends MongoRepository<FailedNotification, String> {
}
