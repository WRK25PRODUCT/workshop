package com.gft.workshop.product.integration.messaging.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    public static final String EXCHANGE_PRODUCT = "product";

    @Bean
    public TopicExchange productExchange() {
        return new TopicExchange(EXCHANGE_PRODUCT);
    }
}
