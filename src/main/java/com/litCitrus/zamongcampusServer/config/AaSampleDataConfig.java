package com.litCitrus.zamongcampusServer.config;

import com.litCitrus.zamongcampusServer.domain.user.Authority;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.io.dynamodb.service.DynamoDBHandler;
import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserInterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserPictureRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@RequiredArgsConstructor
@Configuration
class AaSampleDataConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPictureRepository userPictureRepository;
    private final DynamoDBHandler dynamoDBHandler;

    @Bean
    @Order(1)
    /**
     * dummyuser 생성 (5 ~ 15) < user4 = id5
     */
    CommandLineRunner commandLineRunnerForInterests() {
        return args -> {
            List<String> nicknames = Arrays.asList("한소희", "이해리", "아이유", "마동석", "송중기", "박민영", "정용화", "공유", "뷔", "차은우", "정해인", "웬디");
            Random random = new Random();
            for(int i = 4; i < 15; i ++){
                UserDtoReq.Create dto = new UserDtoReq.Create(
                        "user" + Integer.toString(i),
                        "1234",
                        nicknames.get(i - 4),
                        "sampledeviceToken",
                        "COLLEGE000" + Integer.toString((random.nextInt(4) + 1)),
                        "MAJOR000" + Integer.toString((random.nextInt(4) + 1)),
                        null,
                        null,
                        "테스트 유저 " + Integer.toString(i) + "번 입니다!",
                        null
                        );
                Authority authority = Authority.builder()
                        .authorityName("ROLE_USER")
                        .build();
                User user = User.createUser(dto, passwordEncoder.encode("1234"), authority);
                user.updateActivated(true);
                userRepository.save(user);

                UserPicture userPicture = UserPicture.createUserPicture(user, "https://d1cy8kjxuu1lsp.cloudfront.net/2022/user/20220504/a43e5a59-3b08-4fac-9c36-27468edc11da83851993430251user"+Integer.toString(i)+".jpg");
                userPictureRepository.save(userPicture);
            }
        };
    }

//    @Bean
//    @Order(5)
//    /**
//     * dynamodb sample message 저장
//     */
//    CommandLineRunner commandLineRunnerForDynamodbChatMessage() {
//        return args -> {
//            /* enter message */
//            ChatMessageDtoReq chatMessageDtoReq1 = new ChatMessageDtoReq();
//            chatMessageDtoReq1.setRoomId("room001");
//            chatMessageDtoReq1.setText("홍길동님이 입장하셨습니다!");
//            chatMessageDtoReq1.setType("enter");
//            dynamoDBHandler.putMessage(chatMessageDtoReq1);
//            /* talk message */
//            ChatMessageDtoReq chatMessageDtoReq2 = new ChatMessageDtoReq();
//            chatMessageDtoReq2.setLoginId("zm1");
//            chatMessageDtoReq2.setRoomId("room001");
//            chatMessageDtoReq2.setText("hi~ 자몽캠퍼스");
//            chatMessageDtoReq2.setType("talk");
//            chatMessageDtoReq2.setChatRoomType("single");
//            dynamoDBHandler.putMessage(chatMessageDtoReq2);
//            /* exit message */
//            ChatMessageDtoReq chatMessageDtoReq3 = new ChatMessageDtoReq();
//            chatMessageDtoReq3.setRoomId("room001");
//            chatMessageDtoReq3.setText("홍길동님이 퇴장하셨습니다!");
//            chatMessageDtoReq3.setType("exit");
//            dynamoDBHandler.putMessage(chatMessageDtoReq3);
//
//        };
//    }

}