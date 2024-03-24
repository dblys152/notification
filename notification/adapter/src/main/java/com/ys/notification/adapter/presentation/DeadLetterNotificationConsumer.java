package com.ys.notification.adapter.presentation;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DeadLetterNotificationConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.dead-letter-notification}")
    public void onDeadLetterMessage(Message message, Channel channel) throws IOException {
        log.error("Fail notification consumer. Message: {}", new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
