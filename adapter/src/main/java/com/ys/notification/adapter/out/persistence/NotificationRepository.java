package com.ys.notification.adapter.out.persistence;

import com.ys.notification.domain.NotificationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findAllByStatusAndSentAtLessThanEqual(NotificationStatus status, LocalDateTime sentAt);
}
