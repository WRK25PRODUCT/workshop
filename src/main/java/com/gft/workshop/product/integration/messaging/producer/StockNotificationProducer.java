package com.gft.workshop.product.integration.messaging.producer;


import com.gft.workshop.product.business.dto.StockNotificationDTO;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class StockNotificationProducer {

    private final RabbitTemplate rabbitTemplate;

    public StockNotificationProducer(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendRestockNotification(Long productId, int stock)
    {
        sendNotification("products", productId, stock, "stock.restock");
    }

    public void sendBelowThresholdNotification(Long productId, int stock)
    {
        sendNotification("product.low", productId, stock, "stock.low");
    }

    public void sendStockUpdateNotification(Long productId, int stock)
    {
        sendNotification("product.changed",productId, stock, "stock.change");
    }

    private void sendNotification(String exchange , Long productId, int stock, String routingKey)
    {
        StockNotificationDTO dto = new StockNotificationDTO(productId, stock);
        rabbitTemplate.convertAndSend(exchange, routingKey, dto);
    }
}
