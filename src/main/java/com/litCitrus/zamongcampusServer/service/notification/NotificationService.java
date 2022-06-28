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

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public List<NotificationDtoRes> getMyNotification(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        return notificationRepository.findAllByUser(user).stream()
                .map(NotificationDtoRes::new).collect(Collectors.toList());
    }

    public void deleteMyNotification(Long notificationId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Notification notification = notificationRepository.findById(notificationId).get();
        if(notification.getUser().getLoginId() == user.getLoginId()){
            notificationRepository.delete(notification);
        }
    }

}
