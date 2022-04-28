//package com.litCitrus.zamongcampusServer.config;
//
//import com.litCitrus.zamongcampusServer.domain.interest.Interest;
//import com.litCitrus.zamongcampusServer.domain.user.User;
//import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
//import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
//import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
//import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
//import com.litCitrus.zamongcampusServer.io.dynamodb.service.DynamoDBHandler;
//import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
//import com.litCitrus.zamongcampusServer.repository.user.UserInterestRepository;
//import com.litCitrus.zamongcampusServer.repository.user.UserPictureRepository;
//import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
//import com.litCitrus.zamongcampusServer.service.user.SignUpService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.annotation.Order;
//
//import java.util.*;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//@RequiredArgsConstructor
//@Configuration
//class AaSampleDataConfig {
//
////    private final UserRepository userRepository;
////    private final UserPictureRepository userPictureRepository;
////    private final InterestRepository interestRepository;
////    private final UserInterestRepository userInterestRepository;
//    private final DynamoDBHandler dynamoDBHandler;
////
////    User user1 = User.createUser(new UserDtoReq.Create
////            ("zm1", "1234", "devicetoken1", "mmong1@naver.com",
////                    "가나초코릿", "홍길동", "college0001", "경영학과", 32220001));
////
////    User user2 = User.createUser(new UserDtoReq.Create
////            ("zm2", "1234", "devicetoken2", "mmong2@naver.com",
////                    "나비야훨훨", "김철수", "college0002", "무역학과", 32220002));
////
////    User user3 = User.createUser(new UserDtoReq.Create
////            ("zm3", "1234", "devicetoken3", "mmong3@naver.com",
////                    "다부지", "김영미", "college0003", "소프트웨어학과", 32220003));
////
////    User user4 = User.createUser(new UserDtoReq.Create
////            ("zm4", "1234", "devicetoken4", "mmong4@naver.com",
////                    "라디오꾼", "최기신", "college0004", "성악과", 32220004));
////    User admin = userRepository.findOneWithAuthoritiesByLoginId("admin").get();
////    @Bean
////    @Order(1)
////    /**
////     * interest 생성
////     */
////    CommandLineRunner commandLineRunnerForInterests() {
////        return args -> {
////            List<Interest> interests = Interest.createInterests(
////                    Stream.of(
////                            "친목/일상", "문화", "암호화폐", "워홀",
////                            "진로상담", "한달살기", "게임", "오버워치",
////                            "배그", "연애", "맛집투어", "카페투어",
////                            "치느님", "다이어트", "공부", "미술/예술",
////                            "코로나", "디저트", "필라테스", "스키/보드"
////                    ).collect(Collectors.toList())
////            );
////            interestRepository.saveAll(interests);
////        };
////    }
////
////    @Bean
////    @Order(2)
////    /**
////     * user1,user2,user3,user4 저장
////     */
////    CommandLineRunner commandLineRunnerForUser() {
////        return args -> {
////            userRepository.save(user1);
////            userRepository.save(user2);
////            userRepository.save(user3);
////            userRepository.save(user4);
////        };
////    }
////
////    @Bean
////    @Order(3)
////    /**
////     * user1,user2에게 interest 저장
////     */
////    CommandLineRunner commandLineRunnerForUserInterest() {
////        return args -> {
////            /* user1 interest */
////            Set<Interest> targetSet = new HashSet(interestRepository.findAllById(Arrays.asList(1L, 2L, 3L)));
////            Set<UserInterest> userInterests = targetSet.stream().map(interest -> new UserInterest(user1, interest)).collect(Collectors.toSet());
////            user1.updateUserInterests(userInterests);
////            userInterestRepository.saveAll(userInterests);
////            userRepository.save(user1);
////
////            /* user2 interest */
////            Set<Interest> targetSet2 = new HashSet(interestRepository.findAllById(Arrays.asList(4L, 5L, 6L)));
////            Set<UserInterest> userInterests2 = targetSet2.stream().map(interest -> new UserInterest(user2, interest)).collect(Collectors.toSet());
////            user2.updateUserInterests(userInterests2);
////            userRepository.save(user2);
////            userInterestRepository.saveAll(userInterests2);
////        };
////    }
////
////    @Bean
////    @Order(4)
////    /**
////     * user1,user2,user3,user4 picture 저장
////     */
////    CommandLineRunner commandLineRunnerForUserPicture() {
////        return args -> {
////            /* user1 picture */
////            List<UserPicture> pictures1 = new ArrayList<UserPicture>(Arrays.asList(UserPicture.createUserPicture(user1, "https://atti-test-orangebooksorg-images.s3.ap-northeast-2.amazonaws.com/2022/user/20220304/user1.jpg")));
////            user1.addUserPictures(pictures1);
////            userPictureRepository.saveAll(pictures1);
////            userRepository.save(user1);
////
////            /* user2 picture */
////            List<UserPicture> pictures2 = new ArrayList<UserPicture>(Arrays.asList(UserPicture.createUserPicture(user2, "https://atti-test-orangebooksorg-images.s3.ap-northeast-2.amazonaws.com/2022/user/20220304/user2.jpg")));
////            user2.addUserPictures(pictures2);
////            userPictureRepository.saveAll(pictures2);
////            userRepository.save(user2);
////
////            /* user2 picture */
////            List<UserPicture> pictures3 = new ArrayList<UserPicture>(Arrays.asList(UserPicture.createUserPicture(user3, "https://atti-test-orangebooksorg-images.s3.ap-northeast-2.amazonaws.com/2022/user/20220304/user3.jpg")));
////            user3.addUserPictures(pictures3);
////            userPictureRepository.saveAll(pictures3);
////            userRepository.save(user3);
////
////            /* user2 picture */
////            List<UserPicture> pictures4 = new ArrayList<UserPicture>(Arrays.asList(UserPicture.createUserPicture(user4, "https://atti-test-orangebooksorg-images.s3.ap-northeast-2.amazonaws.com/2022/user/20220304/user4.jpg")));
////            user4.addUserPictures(pictures4);
////            userPictureRepository.saveAll(pictures4);
////            userRepository.save(user4);
////        };
////    }
//
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
//
//}