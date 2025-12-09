package com.michelmaia.timecare_notification.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.Map;

@Document(collection = "failed_notifications")
public class FailedNotification {
    @Id
    private String id;

    private Map<String, Object> payload;
    private String reason;
    private Instant timestamp;

    public FailedNotification(Map<String, Object> payload, String reason) {
        this.payload = payload;
        this.reason = reason;
        this.timestamp = Instant.now();
    }

    public String getId() {
        return id;
    }

    public Map<String, Object> getPayload() {
        return payload;
    }

    public String getReason() {
        return reason;
    }

    public Instant getTimestamp() {
        return timestamp;
    }
}
