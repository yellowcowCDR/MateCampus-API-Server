package com.litCitrus.zamongcampusServer.api.notification;

import com.litCitrus.zamongcampusServer.dto.notification.NotificationDtoRes;
import com.litCitrus.zamongcampusServer.service.notification.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/notification")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/my")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<List<NotificationDtoRes>> getMyNotification(){
        return new ResponseEntity<>(notificationService.getMyNotification(), HttpStatus.OK);
    }
}


