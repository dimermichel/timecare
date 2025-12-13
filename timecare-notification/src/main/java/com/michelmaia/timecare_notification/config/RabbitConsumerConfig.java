package com.michelmaia.timecare_notification.config;

import com.michelmaia.timecare_notification.dto.NotificationMessage;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.DefaultJackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class RabbitConsumerConfig {

    @Bean
    public MessageConverter jacksonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTrustedPackages(
                "com.michelmaia.timecare_notification.dto",
                "com.michelmaia.timecare_core.messaging.dto"
        );

        // Map the producer’s __TypeId__ to the consumer’s local DTO class
        typeMapper.setIdClassMapping(Map.of(
                "com.michelmaia.timecare_core.messaging.dto.NotificationMessage", NotificationMessage.class
        ));

        converter.setJavaTypeMapper(typeMapper);
        return converter;
    }

    @Bean(name = "rabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(jacksonMessageConverter);
        return factory;
    }
}
