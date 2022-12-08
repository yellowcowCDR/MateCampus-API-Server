package com.litCitrus.zamongcampusServer.api.notification;

import com.google.firebase.FirebaseException;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.litCitrus.zamongcampusServer.dto.notification.NotificationDtoRes;
import com.litCitrus.zamongcampusServer.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {
    Logger logger = LoggerFactory.getLogger(NotificationController.class);
    private final NotificationService notificationService;

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NotificationDtoRes>> getMyNotification(){
        return new ResponseEntity<>(notificationService.getMyNotification(), HttpStatus.OK);
    }

    @GetMapping("/my/unread")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> getMyUnreadNoti(){
        return new ResponseEntity<>(notificationService.getMyUnreadNoti(), HttpStatus.OK);
    }

    @PutMapping("/my/{notificationId}")
    public ResponseEntity<Long> updateMyNotiRead(@Valid @PathVariable("notificationId") Long notificationId){
        return ResponseEntity.ok(notificationService.updateMyNotiRead(notificationId));
    }

    @PutMapping("/my/all")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateAllMyNotiRead(){
        notificationService.updateAllMyNotiRead();
    }

    @DeleteMapping("/my")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMyNotification(@Valid @PathVariable("notificationId") Long notificationId){
        notificationService.deleteMyNotification(notificationId);
    }

    @GetMapping("/fcmTest")
    @ResponseStatus(HttpStatus.OK)
    public void fcmTest(@ModelAttribute String deviceToken){
        Message message = Message.builder()
                .putData("score", "850")
                .putData("time", "2:45")
                .setToken(deviceToken)
                .build();
        try {
            String response = FirebaseMessaging.getInstance().send(message);
        }catch (FirebaseException e){
            logger.debug("FirebaseException Occurred...");
        }

    }
}


