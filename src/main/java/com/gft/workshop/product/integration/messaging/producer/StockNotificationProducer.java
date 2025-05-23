package com.gft.workshop.product.integration.messaging.producer;


import com.gft.workshop.product.business.dto.StockNotificationDTO;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationProducer {

    private final TopicExchange productExchange;
    private final RabbitTemplate rabbitTemplate;

    public StockNotificationProducer(RabbitTemplate rabbitTemplate, TopicExchange productExchange)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.productExchange = productExchange;
    }

    public void sendRestockNotification(Long productId, int stock)
    {
        sendNotification(productId, stock, "stock.restock");
    }

    public void sendBelowThresholdNotification(Long productId, int stock)
    {
        sendNotification(productId, stock, "stock.low");
    }

    public void sendStockUpdateNotification(Long productId, int stock)
    {
        sendNotification(productId, stock, "stock.change");
    }

    private void sendNotification(Long productId, int stock, String routingKey)
    {
        StockNotificationDTO dto = new StockNotificationDTO(productId, stock);
        rabbitTemplate.convertAndSend(productExchange.getName(), routingKey, dto);
    }
}
