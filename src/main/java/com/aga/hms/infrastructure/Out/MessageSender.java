package com.aga.hms.infrastructure.Out;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageSender {

    private final RabbitTemplate rabbitTemplate;
    private final String exchange;
    private final String routingKey;

    public MessageSender(RabbitTemplate rabbitTemplate,
                         @Value("${rabbitmq.exchange}") String exchange,
                         @Value("${rabbitmq.routing-key}") String routingKey) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = exchange;
        this.routingKey = routingKey;
    }

    public void sendMessage(String message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}

