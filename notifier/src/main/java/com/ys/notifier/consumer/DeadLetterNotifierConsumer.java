package com.ys.notifier.consumer;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class DeadLetterNotifierConsumer {
    @RabbitListener(queues = "${rabbitmq.queue.dead-letter-notifier}")
    public void onDeadLetterMessage(Message message, Channel channel) throws IOException {
        log.error("Fail notifier consumer. Message: {}", new String(message.getBody()));
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
