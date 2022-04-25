//package com.litCitrus.zamongcampusServer.service.chat;
//
//import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
//import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
//import com.litCitrus.zamongcampusServer.domain.user.User;
//import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
//import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoRes;
//import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
//import com.litCitrus.zamongcampusServer.io.dynamodb.model.ChatMessage;
//import com.litCitrus.zamongcampusServer.io.dynamodb.service.DynamoDBHandler;
//import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
//import com.litCitrus.zamongcampusServer.repository.user.ModifiedChatInfoRepository;
//import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.messaging.simp.SimpMessageSendingOperations;
//import org.springframework.stereotype.Service;
//import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class ChatMessageService {
//
//    private final DynamoDBHandler dynamoDBHandler;
//    private final ChatRoomRepository chatRoomRepository;
//    private final UserRepository userRepository;
//    private final SimpMessageSendingOperations messagingTemplate;
//    private final ModifiedChatInfoRepository modifiedChatInfoRepository;
//
//    public void sendMessage(ChatMessageDtoReq messageDto){
//        /**
//         * 역할: 사용자가 메시지를 작성하면, 모바일앱에서 메시지를 받아 다른 사용자에게 해당 메시지를 실시간으로 전달
//         * Dynamo Db에 저장 후 메시지를 채팅방 또는 상대방에게 Stomp으로 전송한다.
//         * TODO: 2021.12.22: 정윤아: 유저 검증 작업 필요
//         */
//        User user = userRepository.findByLoginId(messageDto.getLoginId()).orElseThrow(UserNotFoundException::new);
//        final String currentTime = LocalDateTime.now().toString();
//        /* dto 변환 */
//        ChatMessageDtoRes.MessageDto messageDtoRes = new ChatMessageDtoRes.MessageDto(messageDto.getType(), messageDto.getLoginId(), messageDto.getText(), currentTime);
//        ChatMessageDtoRes.RoomIdMessageBundleDto roomIdMessageBundleDto = ChatMessageDtoRes.RoomIdMessageBundleDto.builder().type(ModifiedChatInfo.MemberStatus.TALK).roomId(messageDto.getRoomId()).messageDto(messageDtoRes).build();
//
//        /* 채팅 메시지를 채팅방에 소속된 사용자에게 전송 (roomId에 메세지 publish) */
//        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), roomIdMessageBundleDto);
//
//        /* 채팅 메시지 디비에 저장 */
//        dynamoDBHandler.putMessage(messageDto);
//    }
//
//    // READ: GET MESSAGE
//    public ChatMessageDtoRes.ChatBundle getChatMessageDynamo(String loginId, String createdAfter){
//        /* 참여한 모든 방 찾기 */
//        User user = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
//        List<ChatRoom> chatRooms = chatRoomRepository.findAllByParticipant_Users(user);
//
//        /* 각 채팅 roomId 기준으로 DynamoDB에서 메시지 가져오고 dto로 변환 */
//        List<ChatMessageDtoRes.RoomMessageBundle> roomMessages
//                = chatRooms.stream()
//                .map(chatRoom ->makeRoomMessageBundle(chatRoom, createdAfter))
//                .filter(chatmessage -> chatmessage!=null)
//                .collect(Collectors.toList());
//
//        /* ModifiedChatInfo에 대한 정보를 MySQL에서 가져오고 dto로 변환 */
//        List<ChatMessageDtoRes.ModifiedInfo> modifiedInfos
//                = user.getModifiedChatInfos().stream().map(modifiedChatInfo ->
//                new ChatMessageDtoRes.ModifiedInfo(modifiedChatInfo, user))
//                .collect(Collectors.toList());
//
//        // Member info 내용을 MySQL에서 삭제
//        //TODO: 반드시 주석 변경해둘 것
//        modifiedChatInfoRepository.deleteAll(user.getModifiedChatInfos());
//
//        return new ChatMessageDtoRes.ChatBundle(roomMessages, modifiedInfos);
//
//    }
//
//    private ChatMessageDtoRes.RoomMessageBundle makeRoomMessageBundle(ChatRoom chatRoom, String createdAfter){
//        PageIterable<ChatMessage> chatMessagesByRoomId =
//                dynamoDBHandler.getMessages(chatRoom.getRoomId(), createdAfter);
//        if(chatMessagesByRoomId.items().stream().count() == 0){
//            return null;
//        }else{
//            return new ChatMessageDtoRes.RoomMessageBundle(chatRoom.getRoomId(), chatMessagesByRoomId);
//        }
//    }
//}
