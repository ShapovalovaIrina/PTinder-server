package com.trkpo.ptinder.repository;

import com.trkpo.ptinder.entity.Notifications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationsRepository extends JpaRepository<Notifications, Long> {
}
