package com.ys.notification.adapter.out.persistence;

import com.ys.notification.adapter.config.DataJpaConfig;
import com.ys.notification.adapter.out.persistence.fixture.SupportNotificationFixture;
import com.ys.notification.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(classes = DataJpaConfig.class)
class NotificationRepositoryTest extends SupportNotificationFixture {
    @Autowired
    private NotificationRepository repository;

    private NotificationEntity notificationEntity;

    @BeforeEach
    void setUp() {
        Notification notification = Notification.create(new CreateNotificationCommand(
                NotificationType.EMAIL, SENT_AT, SenderType.SYSTEM, SENDER_USER_ID, DESTINATION_EMAIL, ReceiverType.USER, RECEIVER_ID, TITLE, CONTENTS));
        notificationEntity = NotificationEntity.fromDomain(notification);
    }

    @Test
    void svae() {
        NotificationEntity actual = repository.save(notificationEntity);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void svae_and_save() {
        NotificationEntity savedEntity = repository.save(notificationEntity);

        NotificationEntity actual = repository.save(savedEntity);

        assertThat(actual).isNotNull();
        assertThat(actual.getId()).isNotNull();
    }

    @Test
    void findById() {
        NotificationEntity savedEntity = repository.save(notificationEntity);

        Optional<NotificationEntity> actual = repository.findById(savedEntity.getId());

        assertThat(actual).isPresent();
    }
}