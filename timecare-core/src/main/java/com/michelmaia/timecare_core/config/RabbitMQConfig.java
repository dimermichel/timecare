package com.michelmaia.timecare_core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    // Queue name constant
    public static final String NOTIFICATION_QUEUE = "notifications.queue";
    public static final String NOTIFICATION_EXCHANGE = "notifications.exchange";
    public static final String NOTIFICATION_ROUTING_KEY = "notification.#";

    /**
     * Declare the notification queue
     */
    @Bean
    public Queue notificationQueue() {
        return new Queue(NOTIFICATION_QUEUE, true, false, false);
        // true = durable (persists after restart)
        // false = not exclusive
        // false = don't auto-delete
    }

    /**
     * Declare the topic exchange
     */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE, true, false);
        // true = durable
        // false = don't auto-delete
    }

    /**
     * Bind the queue to the exchange
     */
    @Bean
    public Binding notificationBinding(Queue notificationQueue, TopicExchange notificationExchange) {
        return BindingBuilder.bind(notificationQueue)
                .to(notificationExchange)
                .with(NOTIFICATION_ROUTING_KEY);
    }

    /**
     * Message converter for JSON serialization
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
