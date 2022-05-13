package com.litCitrus.zamongcampusServer.dto.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.io.dynamodb.model.ChatMessage;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 모바일앱에 채팅 메시지를 보내기 위한 DTO
 * - 실시간 RealTimeMessageBundle는 Talk 타입을 외부에 갖기 위해 SystemMessageDto를 상속
 * (client 입장에서 type에 따라 바로 구분 짓을 수 있도록 => 즉 type이 두번 보내짐. messageDto 내부, realTimeMessageBundle 내부)
 * - 반면 앱 킬 때의 RoomMessageBundle은 List의 messageDto를 갖기에
 * - messageDto 안의 type으로 구분 짓게 해야한다.
 */
@Getter
public class ChatMessageDtoRes {

    /** sendMessageDto(실시간) 시작 */
    @Getter
    @SuperBuilder
    public static class RealTimeMessageBundle extends SystemMessageDto.SystemMessage {
        private String roomId;
        private MessageDto messageDto; /// ENTER, EXIT, UPDATE, CREATE, TALK
    }

    @Getter
    @AllArgsConstructor
    public static class MessageDto{
        private String type;
        private String loginId;
        private String text;
        private String createdAt;
        public MessageDto(ChatMessage message){
            this.type = message.getType();
            this.loginId = message.getLoginId();
            this.text = message.getText();
            this.createdAt = message.getCreatedAt();
        }
    }

    /** sendMessageDto(실시간) 끝 */


    /** getChatMessageDto(앱 킬 때) 시작 */
    @Getter
    @AllArgsConstructor
    public static class ChatBundle{
        private List<RoomMessageBundle> roomMessageBundles;
        private List<ModifiedInfo> modifiedInfos;
    }

    @Getter
    public static class RoomMessageBundle{
        private String roomId;
        private List<MessageDto> messages; /// messageDto의 type이 일반메세지는 ENTER, EXIT, TALK 만 존재.
        public RoomMessageBundle(String roomId, PageIterable<ChatMessage> chatMessages){
            this.roomId = roomId;
            this.messages = chatMessages.items().stream().map(item -> new MessageDto(item)).collect(Collectors.toList()); // PageIterable<ChatMessage> 반환
        }
    }

    // modifiedInfo가 enter,exit,update,create마다 조금씩 달라야한다.
    @Getter
    public static class ModifiedInfo{
        private SystemMessageDto.SystemMessage systemMessage;
        private ModifiedChatInfo.MemberStatus memberStatus;

        public ModifiedInfo(ModifiedChatInfo modifiedChatInfo, User actor){
            systemMessage = makeSystemMessage(modifiedChatInfo, actor);
            memberStatus = modifiedChatInfo.getMemberStatus();
        }

        public SystemMessageDto.SystemMessage makeSystemMessage(ModifiedChatInfo modifiedChatInfo, User actor){
            if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.ENTER)){
                return SystemMessageDto.EnterDto.builder()
                        .type(ModifiedChatInfo.MemberStatus.ENTER)
                        .roomId(modifiedChatInfo.getChatRoom().getRoomId())
                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
                        .nickname(modifiedChatInfo.getModifiedUser().getNickname())
                        .imageUrl(modifiedChatInfo.getModifiedUser().getPictures().get(0).getStored_file_path())
                        .build();
            }else if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.EXIT)){
                return SystemMessageDto.ExitDto.builder()
                        .type(ModifiedChatInfo.MemberStatus.EXIT)
                        .roomId(modifiedChatInfo.getChatRoom().getRoomId())
                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
                        .build();
            }else if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.UPDATE)){
                return SystemMessageDto.UpdateDto.builder()
                        .type(ModifiedChatInfo.MemberStatus.UPDATE)
                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
                        .nickname(modifiedChatInfo.getModifiedUser().getNickname())
                        .imageUrl(modifiedChatInfo.getModifiedUser().getPictures().get(0).getStored_file_path())
                        .build();
            }else{
                ChatRoom chatRoom = modifiedChatInfo.getChatRoom();
                List<User> members = Arrays.asList(chatRoom.getUsers().get(0), chatRoom.getUsers().get(1));
                List<String> chatRoomTitleAndImage = chatRoom.getCounterpartChatRoomTitleAndImage(actor.getLoginId()); // 여기의 actor는 메세지 보낸 사람이 아닌 메세지 받는 사람을 지칭
                SystemMessageDto.RoomInfo roomInfo = new SystemMessageDto.RoomInfo(
                        chatRoom.getRoomId(), chatRoom.getType(), chatRoomTitleAndImage.get(0), chatRoomTitleAndImage.get(1));
                List<SystemMessageDto.MemberInfo> memberInfos = members.stream()
                        .map(member -> new SystemMessageDto.MemberInfo(
                                member.getLoginId(), member.getNickname(), member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());
                return SystemMessageDto.CreateDto.builder()
                        .type(ModifiedChatInfo.MemberStatus.CREATE)
                        .roomInfo(roomInfo)
                        .memberInfos(memberInfos)
                        .build();
            }

        }
    }

    /** getChatMessageDto(앱 킬 때) 끝 */


}