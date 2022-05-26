package com.litCitrus.zamongcampusServer.service.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoRes;
import com.litCitrus.zamongcampusServer.dto.chat.SystemMessageDto;
import com.litCitrus.zamongcampusServer.io.dynamodb.service.DynamoDBHandler;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class SystemMessageComponent {

    private final SimpMessageSendingOperations messagingTemplate;
    private final DynamoDBHandler dynamoDBHandler;

    /* ENTER */
    @Transactional
    public void sendSaveEnterSystemMessage(User newMember, ChatRoom chatRoom){
        final String currentTime = LocalDateTime.now().toString();
        SystemMessageDto.EnterDto enterDto = SystemMessageDto.EnterDto.builder()
                .type(ModifiedChatInfo.MemberStatus.ENTER)
                .roomId(chatRoom.getRoomId())
                .loginId(newMember.getLoginId())
                .nickname(newMember.getNickname())
                .createdAt(currentTime)
                .imageUrl(newMember.getPictures().get(0).getStored_file_path())
                .body(newMember.getNickname()+"님이 입장하셨습니다.")
                .build();

        /* 1. stomp 실시간 전달 (일반메세지와 modifiedChatInfo에 해당되는 정보 혼합형) */
        /* (enterDto: roomId,loginId,nickname,imageUrl,createdAt,body)    */
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomId(), enterDto);

        /* 2. 입장메세지(일반) dynamodb에 message 저장 */
        ChatMessageDtoReq messageDto = new ChatMessageDtoReq(chatRoom.getRoomId(), newMember.getNickname()+"님이 입장하셨습니다.", "ENTER", chatRoom.getType());
        dynamoDBHandler.putMessage(messageDto, null, currentTime);

        /* 3. 각 유저의 modifiedChatInfos에 저장 */
        for(User member: chatRoom.getUsers()){
            ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.createEnterExit(ModifiedChatInfo.MemberStatus.ENTER, newMember, chatRoom, member);
            member.addModifiedChatInfo(modifiedChatInfo);
        }

    }
    /* EXIT */
    @Transactional
    public void sendSaveExitSystemMessage(User exitMember, ChatRoom chatRoom){
        final String currentTime = LocalDateTime.now().toString();
        SystemMessageDto.ExitDto exitDto = SystemMessageDto.ExitDto.builder()
                .type(ModifiedChatInfo.MemberStatus.EXIT)
                .roomId(chatRoom.getRoomId())
                .loginId(exitMember.getLoginId())
                .nickname(exitMember.getNickname())
                .createdAt(currentTime)
                .body(exitMember.getNickname()+"님이 퇴장하셨습니다.")
                .build();
        /* 1. stomp 실시간 전달 (exitDto: roomId,loginId,nickname,createdAt,body)*/
        messagingTemplate.convertAndSend("/sub/chat/room/" + chatRoom.getRoomId(), exitDto);

        /* 2. 퇴장메세지(일반) dynamodb에 message 저장 */
        ChatMessageDtoReq messageDto = new ChatMessageDtoReq(chatRoom.getRoomId(), exitMember.getNickname()+"님이 퇴장하셨습니다.", "EXIT", chatRoom.getType());
        dynamoDBHandler.putMessage(messageDto, null, currentTime);

        /* 3. 각 유저의 modifiedChatInfos에 저장 */
        for(User member: chatRoom.getUsers()){
            ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.createEnterExit(ModifiedChatInfo.MemberStatus.EXIT, exitMember, chatRoom, member);
            member.addModifiedChatInfo(modifiedChatInfo);
        }
    }


    /** "ENTER, EXIT" 과 "UPDATE"의 차이는
     * 1. 겹치는 유저가 존재해서 Set으로 중복 제거 필요
     * 2. roomId 대신 deviceToken에 send
     * 3. 일반 메세지 저장하지 않는다는 점
     */
    /* UPDATE: member정보 전달 */
    @Transactional
    public void sendSaveUpdateSystemMessage(User updatedMember, Set<User> recipients){
        SystemMessageDto.UpdateDto updateDto = SystemMessageDto.UpdateDto.builder()
                .type(ModifiedChatInfo.MemberStatus.UPDATE)
                .loginId(updatedMember.getLoginId())
                .nickname(updatedMember.getNickname())
                .imageUrl(updatedMember.getPictures().get(0).getStored_file_path())
                .build();
        /* stomp 실시간 전달. user 개별로 보냄. (roomID로 보내면 겹치는 user가 많기 때문) */
        recipients.stream().forEach(recipient -> messagingTemplate.convertAndSend("/sub/chat/room/" + recipient.getDeviceToken(), updateDto));

        /* 각 유저의 modifiedChatInfos에 저장 */
        for(User memberUser: recipients){
            ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.createUpdate(ModifiedChatInfo.MemberStatus.UPDATE, updatedMember, memberUser);
            memberUser.addModifiedChatInfo(modifiedChatInfo);
        }
    }

    /* CREATE: 채팅방, 채팅방 멤버 정보 전달, 단 본인 제외 */
    @Transactional
    public void sendSaveCreateSystemMessage(ChatRoom chatRoom, List<User> recipients, User actor, User other){
        List<String> chatRoomTitleAndImage = chatRoom.getCounterpartChatRoomTitleAndImage(other.getLoginId()); // 채팅방 만든이의 사진,닉네임
        SystemMessageDto.RoomInfo roomInfo = new SystemMessageDto.RoomInfo(
                chatRoom.getRoomId(), chatRoom.getType(), chatRoomTitleAndImage.get(0), chatRoomTitleAndImage.get(1));
        List<SystemMessageDto.MemberInfo> memberInfos = recipients.stream()
                .map(member -> new SystemMessageDto.MemberInfo(member.getId(),
                        member.getLoginId(), member.getNickname(), member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());
        SystemMessageDto.CreateDto createDto = SystemMessageDto.CreateDto.builder()
                .type(ModifiedChatInfo.MemberStatus.CREATE)
                .roomInfo(roomInfo)
                .memberInfos(memberInfos)
                .build();

        /* 1. stomp 실시간 전달. user의 devicetoken으로 (단, 본인 제외) */
        List<User> recipientsExceptMe = recipients.stream().filter(recipient -> !recipient.getLoginId().equals(actor.getLoginId())).collect(Collectors.toList());
        recipientsExceptMe.stream()
                .forEach(recipient -> messagingTemplate.convertAndSend("/sub/chat/room/" + recipient.getDeviceToken(), createDto));

        /* 2. 유저들의 modifiedChatInfos에 Create라는 ModifiedChatInfo를 저장 */
        /* (Create Info는 채팅방, 채팅방멤버 정보를 가진다. )*/
        for(User memberUser: recipientsExceptMe){
            ModifiedChatInfo modifiedChatInfo = ModifiedChatInfo.createCreate(ModifiedChatInfo.MemberStatus.CREATE, chatRoom, memberUser);
            memberUser.addModifiedChatInfo(modifiedChatInfo);
        }
    }
}
