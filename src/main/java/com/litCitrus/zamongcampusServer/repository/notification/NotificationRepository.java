package com.litCitrus.zamongcampusServer.repository.notification;

import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findAllByUser(User user);
}
