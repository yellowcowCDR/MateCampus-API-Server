package com.litCitrus.zamongcampusServer.config;

import com.litCitrus.zamongcampusServer.domain.user.*;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.college.CampusRepository;
import com.litCitrus.zamongcampusServer.repository.college.CollegeRepository;
import com.litCitrus.zamongcampusServer.repository.major.MajorRepository;
import com.litCitrus.zamongcampusServer.repository.user.FriendRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserPictureRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Configuration
@Profile("sample-data")
@RequiredArgsConstructor
class AaSampleDataConfig {

    @Value("${dummy.user.admin.key}")
    private String adminKey;

    @Value("${dummy.user.general.key}")
    private String generalKey;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserPictureRepository userPictureRepository;
    private final CollegeRepository collegeRepository;
    private final CampusRepository campusRepository;
    private final MajorRepository majorRepository;
    private final FriendRepository friendRepository;

    @Bean
    @Order(1)
    /**
     * admin 저장
     */
    CommandLineRunner commandLineRunnerForAdminAndMainUsers() {
        return args -> {
            UserDtoReq.Create dto = new UserDtoReq.Create(
                    "admin",
                    "의미없음",
                    "관리자",
                    "sampledeviceToken",
                    "빈값",
                    -1L,
                    "물리교육과",
                    120L,
                    null,
                    null,
                    "관리자입니다.",
                    null,
                    0,
                    true,
                    null
            );
            Authority authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();
            List<Authority> authorities = Arrays.asList(Authority.builder()
                    .authorityName("ROLE_USER")
                    .build(), Authority.builder()
                    .authorityName("ROLE_ADMIN")
                    .build());
            Campus campus = campusRepository.findById(1L).orElseThrow(() -> new NullPointerException());
            User user = User.createAdmin(dto, passwordEncoder.encode(adminKey), campus, majorRepository.getOne(1L), authorities);
            user.updateActivated(true);
            userRepository.save(user);

        };
    }

    @Bean
    @Order(2)
    /**
     * dummyuser 생성 (2 ~ 15) < user1 ~ user14
     */
    CommandLineRunner commandLineRunnerForDummyUsers() {
        return args -> {

            List<String> nicknames = Arrays.asList("가나초코릿", "나는나비","도토리조아","라면한소희", "마이해리", "브이아이유", "슈슉마동석", "아직송중기", "찐박민영", "축하정용화", "쿨가이공유", "튤립뷔", "피곤차은우", "하이정해인", "히웬디");
            Random random = new Random();
            for(int i = 1; i < 15; i ++){
                UserDtoReq.Create dto = new UserDtoReq.Create(
                        "user" + Integer.toString(i),
                        "의미없음",
                        nicknames.get(i - 1),
                        "sampledeviceToken",
                        "한국대학교",
                        0L,
                        "전자공학과",
                        120L,
                        null,
                        null,
                        "테스트 유저 " + Integer.toString(i) + "번 입니다!",
                        null,
                        1,
                        false,
                        null
                );
                Authority authority = Authority.builder()
                        .authorityName("ROLE_USER")
                        .build();
                Campus campus = campusRepository.findById(2L).orElseThrow(() -> new NullPointerException());
                User user = User.createUser(dto, passwordEncoder.encode(generalKey), campus, majorRepository.getOne(2L), authority);
                user.updateActivated(true);
                userRepository.save(user);

                UserPicture userPicture = UserPicture.createUserPicture(user, "https://d1cy8kjxuu1lsp.cloudfront.net/2022/user/20220504/a43e5a59-3b08-4fac-9c36-27468edc11da83851993430251user"+Integer.toString(i)+".jpg");
                userPictureRepository.save(userPicture);
            }
        };
    }

    @Bean
    @Order(2)
    /**
     * friend 생성 (user1-user2, user1-user3)
     * 이건 단순 테스트 용도여서. 실제로 할때는 반드시 삭제 (서버에서 켜지기 전에 오류 터지는 현상있을수도 있기에)
     */
    CommandLineRunner commandLineRunnerForFriends() {
        return args -> {
            Friend friend1 = Friend.builder()
                    .recipient(userRepository.findByLoginId("user1").orElseThrow(UserNotFoundException::new))
                    .requestor(userRepository.findByLoginId("user2").orElseThrow(UserNotFoundException::new))
                    .status(Friend.Status.ACCEPTED)
                    .build();
            friendRepository.save(friend1);

            Friend friend2 = Friend.builder()
                    .recipient(userRepository.findByLoginId("user1").orElseThrow(UserNotFoundException::new))
                    .requestor(userRepository.findByLoginId("user3").orElseThrow(UserNotFoundException::new))
                    .status(Friend.Status.ACCEPTED)
                    .build();
            friendRepository.save(friend2);

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