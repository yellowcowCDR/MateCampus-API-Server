package com.litCitrus.zamongcampusServer.service.notification;

import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.notification.NotificationDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.notification.NotificationRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDtoRes> getMyNotification(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByUserOrderByCreatedAtDesc(user).stream()
                .map(NotificationDtoRes::new).collect(Collectors.toList());
    }

    public List<NotificationDtoRes> getMyUnreadNoti(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByUserAndUnReadIsTrue(user).stream()
                .map(NotificationDtoRes::new).collect(Collectors.toList());
    }

    @Transactional
    public long updateMyNotiRead(Long notificationId){
        // 반환을 남은 안읽은 알림 수로 한다.
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).get();
        notification.changeRead();
        return notificationRepository.findAllByUserAndUnReadIsTrue(user).size();
    }

    @Transactional
    public void updateAllMyNotiRead(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<Notification> unreadNotifications = notificationRepository.findAllByUserAndUnReadIsTrue(user);
        for(Notification notification: unreadNotifications){
            notification.changeRead();
        }
    }

    public void deleteMyNotification(Long notificationId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).get();
        if(notification.getUser().getLoginId() == user.getLoginId()){
            notificationRepository.delete(notification);
        }
    }



}
