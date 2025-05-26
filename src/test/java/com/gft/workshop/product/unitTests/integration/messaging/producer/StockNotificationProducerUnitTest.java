package com.gft.workshop.product.unittests.integration.messaging.producer;

import com.gft.workshop.product.business.dto.StockNotificationDTO;
import com.gft.workshop.product.integration.messaging.producer.StockNotificationProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class StockNotificationProducerUnitTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @Mock
    private TopicExchange productExchange;

    private StockNotificationProducer producer;

    @BeforeEach
    void init() {
        initObjects();
    }

    @Test
    void shouldSendRestockNotification() {
        producer.sendRestockNotification(1L, 10);
        Mockito.verify(rabbitTemplate).convertAndSend("product", "stock.restock", new StockNotificationDTO(1L, 10));
    }

    @Test
    void shouldSendBelowThresholdNotification() {
        producer.sendBelowThresholdNotification(1L, 10);
        Mockito.verify(rabbitTemplate).convertAndSend("product", "stock.low", new StockNotificationDTO(1L, 10));
    }

    @Test
    void shouldSendStockUpdateNotification() {
        producer.sendStockUpdateNotification(1L, 10);
        Mockito.verify(rabbitTemplate).convertAndSend("product", "stock.change", new StockNotificationDTO(1L, 10));
    }

    private void initObjects() {
        Mockito.when(productExchange.getName()).thenReturn("product");
        producer = new StockNotificationProducer(rabbitTemplate, productExchange);
    }
}
