package com.michelmaia.timecare_notification.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("email_logs")
public class EmailLog {

    @Id
    private String id;

    private String email;
    private String subject;
    private String body;
}