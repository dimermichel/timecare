package com.michelmaia.timecare_core.messaging.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NotificationMessage {

    @JsonProperty("email")
    private String email;

    @JsonProperty("subject")
    private String subject;

    @JsonProperty("body")
    private String body;
}