package com.michelmaia.timecare_notification.repository;

import com.michelmaia.timecare_notification.model.EmailLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EmailLogRepository extends MongoRepository<EmailLog, String> {
}
