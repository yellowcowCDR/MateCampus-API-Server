package com.litCitrus.zamongcampusServer.service.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoRes;
import com.litCitrus.zamongcampusServer.exception.chat.ChatRoomNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.io.dynamodb.model.ChatMessage;
import com.litCitrus.zamongcampusServer.io.dynamodb.service.DynamoDBHandler;
import com.litCitrus.zamongcampusServer.io.fcm.FCMDto;
import com.litCitrus.zamongcampusServer.io.fcm.FCMHandler;
import com.litCitrus.zamongcampusServer.repository.chat.ChatRoomRepository;
import com.litCitrus.zamongcampusServer.repository.user.ModifiedChatInfoRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.security.jwt.TokenProvider;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final DynamoDBHandler dynamoDBHandler;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessageSendingOperations messagingTemplate;
    private final ModifiedChatInfoRepository modifiedChatInfoRepository;
    private final TokenProvider tokenProvider;
    private final FCMHandler fcmHandler;

    @Transactional
    public void sendMessage(ChatMessageDtoReq messageDto, String token){
        /* Dynamo Db에 저장 + 메시지를 채팅방(roomId)에게 Stomp으로 전송한다 */
        Authentication authentication = tokenProvider.getAuthentication(token.substring(7));
        User user = SecurityUtil.getCurrentUsername(authentication).flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        final String currentTime = LocalDateTime.now().toString();
        ChatMessageDtoRes.MessageDto messageDtoRes = new ChatMessageDtoRes.MessageDto(messageDto.getType(), user.getLoginId(), messageDto.getText(), currentTime);
        ChatMessageDtoRes.RealTimeMessageBundle roomIdMessageBundleDto = ChatMessageDtoRes.RealTimeMessageBundle.builder()
                .type(ModifiedChatInfo.MemberStatus.TALK)
                .roomId(messageDto.getRoomId())
                .messageDto(messageDtoRes).build();

        /* 1. 채팅 메시지를 채팅방에 소속된 사용자에게 전송 (roomId에 메세지 publish) */
        messagingTemplate.convertAndSend("/sub/chat/room/" + messageDto.getRoomId(), roomIdMessageBundleDto);

        /* 2. 채팅 메시지 디비에 저장 */
        dynamoDBHandler.putMessage(messageDto, user.getLoginId(), currentTime);

        /* 3. fcm(알림) 전송 */
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(messageDto.getRoomId()).orElseThrow(ChatRoomNotFoundException::new);
        List<String> chatRoomTitleAndImage = chatRoom.getCounterpartChatRoomTitleAndImage(user.getLoginId());
        FCMDto fcmDto = new FCMDto(user.getNickname() + " \n " + messageDto.getText(),
                new HashMap<String,String>(){{
                    put("navigate","/chatDetail");
                    put("roomId", chatRoom.getRoomId());
                    put("title", user.getNickname());
                    put("imageUrl", user.getPictures().isEmpty() ? null : user.getPictures().get(0).getStored_file_path());
                    put("type", chatRoom.getType());

        }});
        List<User> recipientsExceptMe  = chatRoomRepository.findByRoomId(messageDto.getRoomId()).orElseThrow(ChatRoomNotFoundException::new)
                .getUsers().stream().filter(recipient -> !recipient.getLoginId().equals(user.getLoginId())).collect(Collectors.toList());
        fcmHandler.sendNotification(fcmDto, "fcm_message_channel",recipientsExceptMe);
    }

    // READ: GET MESSAGE
    public ChatMessageDtoRes.ChatBundle getChatMessageDynamo(String createdAfter){
        /* 1. 참여한 모든 방 찾기 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<ChatRoom> chatRooms = chatRoomRepository.findAllByParticipant_Users(user);

        /* 2. 각 채팅 roomId 기준으로 DynamoDB에서 메시지 가져오고 dto로 변환 */
        List<ChatMessageDtoRes.RoomMessageBundle> roomMessages
                = chatRooms.stream()
                .map(chatRoom ->makeRoomMessageBundle(chatRoom, createdAfter))
                .filter(chatMessage -> chatMessage!=null)
                .collect(Collectors.toList());

        /* 3. ModifiedChatInfo에 대한 정보를 MySQL에서 가져오고 dto로 변환 */
        List<ChatMessageDtoRes.ModifiedInfo> modifiedInfos
                = user.getModifiedChatInfos().stream().map(modifiedChatInfo ->
                new ChatMessageDtoRes.ModifiedInfo(modifiedChatInfo, user))
                .collect(Collectors.toList());

        // Member info 내용을 MySQL에서 삭제
        //TODO: 반드시 주석 변경해둘 것
        modifiedChatInfoRepository.deleteAll(user.getModifiedChatInfos());

        return new ChatMessageDtoRes.ChatBundle(roomMessages, modifiedInfos);

    }

    private ChatMessageDtoRes.RoomMessageBundle makeRoomMessageBundle(ChatRoom chatRoom, String createdAfter){
        PageIterable<ChatMessage> chatMessagesByRoomId =
                dynamoDBHandler.getMessages(chatRoom.getRoomId(), createdAfter);
        if(chatMessagesByRoomId.items().stream().count() == 0){
            return null;
        }else{
            return new ChatMessageDtoRes.RoomMessageBundle(chatRoom.getRoomId(), chatMessagesByRoomId);
        }
    }
}
