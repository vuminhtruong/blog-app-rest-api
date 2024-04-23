package com.truongvu.blogrestapi.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ErrorHandler;

@Service
@RequiredArgsConstructor
public class RabbitMQError implements ErrorHandler {
    private final RabbitTemplate rabbitTemplate;
    @Override
    public void handleError(Throwable t) {
        throw new AmqpRejectAndDontRequeueException("Error Handler converted exception to fatal", t);
    }
}
