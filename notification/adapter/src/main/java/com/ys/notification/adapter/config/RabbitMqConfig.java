package com.ys.notification.adapter.config;

import com.ys.notification.domain.event.NotificationEventType;
import com.ys.shared.queue.RabbitMqExchange;
import com.ys.shared.queue.RabbitMqExchangeNameMapping;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class RabbitMqConfig {
    private static final String QUORUM_QUEUE_TYPE = "quorum";

    @Value("${rabbitmq.queue.send-email-notifier}")
    private String SEND_EMAIL_NOTIFIER_QUEUE;
    @Value("${rabbitmq.queue.send-sms-notifier}")
    private String SEND_SMS_NOTIFIER_QUEUE;
    @Value("${rabbitmq.queue.dead-letter-notifier}")
    private String DEAD_LETTER_NOTIFIER_QUEUE;
    @Value("${rabbitmq.queue.process-notification-sending-result}")
    private String PROCESS_NOTIFICATION_SENDING_RESULT_QUEUE;
    @Value("${rabbitmq.queue.dead-letter-notification}")
    private String DEAD_LETTER_NOTIFICATION_QUEUE;

    @Value("${rabbitmq.exchange.notification.name}")
    private String NOTIFICATION_EXCHANGE;
    @Value("${rabbitmq.exchange.notification.email-routing-key}")
    private String NOTIFICATION_EXCHANGE_EMAIL_ROUTING_KEY;
    @Value("${rabbitmq.exchange.notification.sms-routing-key}")
    private String NOTIFICATION_EXCHANGE_SMS_ROUTING_KEY;
    @Value("${rabbitmq.exchange.notifier.name}")
    private String NOTIFIER_EXCHANGE;
    @Value("${rabbitmq.exchange.notifier.default-routing-key}")
    private String NOTIFIER_EXCHANGE_DEFAULT_ROUTING_KEY;
    @Value("${rabbitmq.exchange.dead-letter.name}")
    private String DEAD_LETTER_EXCHANGE;
    @Value("${rabbitmq.exchange.dead-letter.notifier-routing-key}")
    private String DEAD_LETTER_EXCHANGE_NOTIFIER_ROUTING_KEY;
    @Value("${rabbitmq.exchange.dead-letter.notification-routing-key}")
    private String DEAD_LETTER_EXCHANGE_NOTIFICATION_ROUTING_KEY;

    /* Queue */
    @Bean
    public Queue sendEmailNotifierQueue() {
        return makeQueue(SEND_EMAIL_NOTIFIER_QUEUE, QUORUM_QUEUE_TYPE, 4, DEAD_LETTER_EXCHANGE, DEAD_LETTER_EXCHANGE_NOTIFIER_ROUTING_KEY);
    }

    @Bean
    public Queue sendSmsNotifierQueue() {
        return makeQueue(SEND_SMS_NOTIFIER_QUEUE, QUORUM_QUEUE_TYPE, 4, DEAD_LETTER_EXCHANGE, DEAD_LETTER_EXCHANGE_NOTIFIER_ROUTING_KEY);
    }
    @Bean
    public Queue processNotificationSendingResultQueue() {
        return makeQueue(PROCESS_NOTIFICATION_SENDING_RESULT_QUEUE, QUORUM_QUEUE_TYPE, 4, DEAD_LETTER_EXCHANGE, DEAD_LETTER_EXCHANGE_NOTIFICATION_ROUTING_KEY);
    }
    @Bean
    public Queue deadLetterNotifierQueue() {
        return makeQueue(DEAD_LETTER_NOTIFIER_QUEUE, QUORUM_QUEUE_TYPE, 4);
    }
    @Bean
    public Queue deadLetterNotificationQueue() {
        return makeQueue(DEAD_LETTER_NOTIFICATION_QUEUE, QUORUM_QUEUE_TYPE, 4);
    }
    private Queue makeQueue(String name, String type, int deliveryLimit, String dlxName, String dlxRoutingKey) {
        return QueueBuilder.durable(name)
                .withArgument("x-queue-type", type)
                .withArgument("x-delivery-limit", deliveryLimit)
                .withArgument("x-dead-letter-exchange", dlxName)
                .withArgument("x-dead-letter-routing-key", dlxRoutingKey)
                .build();
    }
    private Queue makeQueue(String name, String type, int deliveryLimit) {
        return QueueBuilder.durable(name)
                .withArgument("x-queue-type", type)
                .withArgument("x-delivery-limit", deliveryLimit)
                .build();
    }

    /* Exchange */
    @Bean
    public TopicExchange notificationExchange() {
        return new TopicExchange(NOTIFICATION_EXCHANGE);
    }
    @Bean
    public TopicExchange notifierExchange() {
        return new TopicExchange(NOTIFIER_EXCHANGE);
    }

    @Bean
    public TopicExchange deadLetterExchange() {
        return new TopicExchange(DEAD_LETTER_EXCHANGE);
    }

    /**
     *  Binding
     *  NotificationExchange -> [SendEmailNotifierQueue, SendSmsNotifierQueue]
     *  NotifierExchange -> [ChangeNotificationStatusQueue]
     *  DeadLetterExchange -> [DeadLetterNotifierQueue, DeadLetterNotificationQueue]
     */
    @Bean
    public Binding sendEmailNotifierQueueToNotificationExchangeBinding() {
        return BindingBuilder.bind(sendEmailNotifierQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_EXCHANGE_EMAIL_ROUTING_KEY);
    }
    @Bean
    public Binding sendSmsNotifierQueueToNotificationExchangeBinding() {
        return BindingBuilder.bind(sendSmsNotifierQueue())
                .to(notificationExchange())
                .with(NOTIFICATION_EXCHANGE_SMS_ROUTING_KEY);
    }
    @Bean
    public Binding processNotificationSendingResultQueueToNotifierExchangeBinding() {
        return BindingBuilder.bind(processNotificationSendingResultQueue())
                .to(notifierExchange())
                .with(NOTIFIER_EXCHANGE_DEFAULT_ROUTING_KEY);
    }
    @Bean
    public Binding deadLetterNotifierQueueToDeadLetterExchangeBinding() {
        return BindingBuilder.bind(deadLetterNotifierQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_EXCHANGE_NOTIFIER_ROUTING_KEY);
    }
    @Bean
    public Binding deadLetterNotificationQueueToDeadLetterExchangeBinding() {
        return BindingBuilder.bind(deadLetterNotificationQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_EXCHANGE_NOTIFICATION_ROUTING_KEY);
    }

    /* Server Connection */
    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.declareQueue(sendEmailNotifierQueue());
        rabbitAdmin.declareQueue(sendSmsNotifierQueue());
        rabbitAdmin.declareQueue(deadLetterNotifierQueue());
        rabbitAdmin.declareQueue(processNotificationSendingResultQueue());
        rabbitAdmin.declareQueue(deadLetterNotificationQueue());
        rabbitAdmin.declareExchange(notificationExchange());
        rabbitAdmin.declareExchange(notifierExchange());
        rabbitAdmin.declareExchange(deadLetterExchange());
        rabbitAdmin.declareBinding(sendEmailNotifierQueueToNotificationExchangeBinding());
        rabbitAdmin.declareBinding(sendSmsNotifierQueueToNotificationExchangeBinding());
        rabbitAdmin.declareBinding(deadLetterNotifierQueueToDeadLetterExchangeBinding());
        rabbitAdmin.declareBinding(processNotificationSendingResultQueueToNotifierExchangeBinding());
        rabbitAdmin.declareBinding(deadLetterNotificationQueueToDeadLetterExchangeBinding());
        return rabbitAdmin;
    }

    // Queue Name Mapping For Message Sender
    @Bean
    public RabbitMqExchangeNameMapping rabbitMqExchangeNameMapping() {
        RabbitMqExchange emailNotificationExchange = RabbitMqExchange.of(NOTIFICATION_EXCHANGE, NOTIFICATION_EXCHANGE_EMAIL_ROUTING_KEY);
        RabbitMqExchange smsNotificationExchange = RabbitMqExchange.of(NOTIFICATION_EXCHANGE, NOTIFICATION_EXCHANGE_SMS_ROUTING_KEY);
        RabbitMqExchangeNameMapping mapping = new RabbitMqExchangeNameMapping();
        mapping.add(NotificationEventType.WAIT_EMAIL_NOTIFICATION_BULK_EVENT.name(), emailNotificationExchange);
        mapping.add(NotificationEventType.WAIT_SMS_NOTIFICATION_BULK_EVENT.name(), smsNotificationExchange);
        return mapping;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (!ack) {
                Message message = correlationData.getReturned().getMessage();
                byte[] body = message.getBody();
                log.error("Fail to produce. ID: {}, Message: {}", correlationData.getId(), body);
            }
        });

        return rabbitTemplate;
    }
}
