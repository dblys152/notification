package com.ys.notification.adapter.infrastructure.persistence;

import com.ys.notification.adapter.config.DataJpaConfig;
import com.ys.notification.adapter.infrastructure.persistence.fixture.SupportNotificationFixture;
import com.ys.notification.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

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
                NotificationType.EMAIL, SENT_AT, DESTINATION_EMAIL, TITLE, CONTENTS, SENDER, RECEIVER));
        notificationEntity = NotificationEntity.fromDomain(notification);
    }

    @Test
    void save() {
        NotificationEntity actual = repository.save(notificationEntity);

        assertThat(actual).isNotNull();
        assertThat(actual.getNotificationId()).isNotNull();
    }

    @Test
    void saveAll() {
        List<NotificationEntity> actual = repository.saveAll(List.of(notificationEntity));

        assertThat(actual).isNotEmpty();
    }

    @Test
    void findById() {
        NotificationEntity savedEntity = repository.save(notificationEntity);

        Optional<NotificationEntity> actual = repository.findById(savedEntity.getNotificationId());

        assertThat(actual).isPresent();
    }

    @Test
    void findAllById() {
        NotificationEntity savedEntity = repository.save(notificationEntity);

        List<NotificationEntity> actual = repository.findAllById(List.of(savedEntity.getNotificationId()));

        assertThat(actual).isNotEmpty();
    }

    @Test
    void findAllByStatusAndSentAtLessThanEqual() {
        repository.save(notificationEntity);

        List<NotificationEntity> actual = repository.findAllByStatusAndSentAtLessThanEqual(NotificationStatus.RESERVED, LocalDateTime.now());

        assertThat(actual).isNotEmpty();
    }
}