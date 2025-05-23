package com.gft.workshop.product.integration.integration.messaging.producer;

import com.gft.workshop.product.integration.messaging.producer.StockNotificationProducer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class StockNotificationProducerIT {

    @Autowired
    private StockNotificationProducer producer;

    @Test
    @DisplayName("should send a restock notification to CloudAMQP")
    public void shouldSendRestockNotification() throws InterruptedException
    {
        producer.sendRestockNotification(1L, 10);
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("should send a below threshold notification to CloudAMQP")
    public void testSendBelowThresholdNotification() throws InterruptedException {
        producer.sendBelowThresholdNotification(1L, 3);
        Thread.sleep(2000);
    }

    @Test
    @DisplayName("should send an stock update notification to CloudAMQP")
    public void testSendStockUpdateNotification() throws InterruptedException {
        producer.sendStockUpdateNotification(1L, 20);
        Thread.sleep(2000);
    }
}
