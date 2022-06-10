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
    final String title = "자몽캠퍼스";
    public void sendNotification(FCMDto fcmDto, String channelId, List<User> recipients) {

        List<String> recipientTokens = recipients.stream().map(user -> user.getDeviceToken()).collect(Collectors.toList());
        Notification notification = Notification
                .builder()
                .setTitle(title)
                .setBody(fcmDto.getBody())
                .build();
        AndroidNotification androidNotification = AndroidNotification.builder()
                .setChannelId(channelId)
                .build();
        AndroidConfig androidConfig = AndroidConfig.builder()
                .setNotification(androidNotification)
                .build();
        if(!recipientTokens.isEmpty()){
            MulticastMessage message = MulticastMessage.builder()
                    .addAllTokens(recipientTokens)
                    .setNotification(notification)
                    .putAllData(fcmDto.getData())
                    .setAndroidConfig(androidConfig)
                    .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                    .build();
            BatchResponse response = null;
            try {
                response = firebaseMessaging.sendMulticast(message);
            } catch (FirebaseMessagingException e) {
                e.printStackTrace();
            }
            System.out.println(response.getSuccessCount());
        }
    }
}
