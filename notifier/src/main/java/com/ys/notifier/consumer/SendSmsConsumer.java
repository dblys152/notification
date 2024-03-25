package com.ys.notifier.consumer;

import com.rabbitmq.client.Channel;
import com.ys.notification.domain.event.NotificationBulkEvent;
import com.ys.shared.event.EventMessageEnvelopProcessReturn;
import com.ys.shared.event.EventMessageEnvelopProcessTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.function.Consumer;

@Component
@RequiredArgsConstructor
@Slf4j
public class SendSmsConsumer {
    //@Setter(value = AccessLevel.PACKAGE)
    private EventMessageEnvelopProcessTemplate template = new EventMessageEnvelopProcessTemplate();

    private final Consumer<NotificationBulkEvent> processor;

    @RabbitListener(queues = "${rabbitmq.queue.send-sms-notifier}")
    public void receive(Message message, Channel channel) throws IOException {
        EventMessageEnvelopProcessReturn processReturn = template.doProcess(new String(message.getBody()), NotificationBulkEvent.class, processor);
        switch (processReturn) {
            case IGNORE, SUCCESS -> channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            case RETRY -> channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        }
    }
}
