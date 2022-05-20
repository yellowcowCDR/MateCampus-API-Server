package com.litCitrus.zamongcampusServer.io.fcm;

import com.google.firebase.messaging.*;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class FCMHandler {

    private final FirebaseMessaging firebaseMessaging;
    final String title = "LitCitrus";
    public void sendNotification(FCMDto fcmDto, List<User> recipients) {

        List<String> recipientTokens = recipients.stream().map(user -> user.getDeviceToken()).collect(Collectors.toList());
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(fcmDto.getBody())
                .build();

        MulticastMessage message = MulticastMessage.builder()
                .addAllTokens(recipientTokens)
                .setNotification(notification)
                .putAllData(fcmDto.getData())
                .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                .build();
        BatchResponse response = null;
        try {
            response = FirebaseMessaging.getInstance().sendMulticast(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
        System.out.println(response.getSuccessCount());
    }
}
