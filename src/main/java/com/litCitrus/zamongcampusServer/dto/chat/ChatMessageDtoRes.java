//package com.litCitrus.zamongcampusServer.dto.chat;
//
//import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
//import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
//import com.litCitrus.zamongcampusServer.domain.user.User;
//import com.litCitrus.zamongcampusServer.io.dynamodb.model.ChatMessage;
//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.experimental.SuperBuilder;
//import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * 모바일앱에 채팅 메시지를 보내기 위한 DTO
// * - 모바일앱 종료 후 재시작했을 때, ChatBundle에 메시지 내용을 담아 전송
// * - 채팅방 멤버 정보가 바뀐 경우 이를 실시간으로 다른 멤버에게 알리기 위해,
// */
//@Getter
//public class ChatMessageDtoRes {
//
//    /** sendMessageDto(실시간) 시작 */
//    @Getter
//    @AllArgsConstructor
//    public static class MessageDto{
//        private String type;
//        private String loginId;
//        private String text;
//        private String createdAt;
//        public MessageDto(ChatMessage message){
//            this.type = message.getType();
//            this.loginId = message.getLoginId();
//            this.text = message.getText();
//            this.createdAt = message.getCreatedAt();
//        }
//    }
//
//    @Getter
//    @SuperBuilder
//    public static class RoomIdMessageBundleDto extends SystemMessageDto.SystemMessage {
//        private String roomId;
//        private MessageDto messageDto;
//    }
//    /** sendMessageDto(실시간) 끝 */
//
//
//    /** getChatMessageDto(앱 킬 때) 시작 */
//    @Getter
//    @AllArgsConstructor
//    public static class ChatBundle{
//        private List<RoomMessageBundle> roomMessageBundles;
//        private List<ModifiedInfo> modifiedInfos;
//    }
//
//    @Getter
//    public static class RoomMessageBundle{
//        private String roomId;
//        private List<MessageDto> messages;
//        public RoomMessageBundle(String roomId, PageIterable<ChatMessage> chatMessages){
//            this.roomId = roomId;
//            this.messages = chatMessages.items().stream().map(item -> new MessageDto(item)).collect(Collectors.toList()); // PageIterable<ChatMessage> 반환
//        }
//    }
//
//    // modifiedInfo가 enter,exit,update,match마다 조금씩 달라야한다.
//    @Getter
//    public static class ModifiedInfo{
//        private SystemMessageDto.SystemMessage systemMessage;
//        private ModifiedChatInfo.MemberStatus memberStatus;
//
//        public ModifiedInfo(ModifiedChatInfo modifiedChatInfo, User actor){
//            systemMessage = makeSystemMessage(modifiedChatInfo, actor);
//            memberStatus = modifiedChatInfo.getMemberStatus();
//        }
//
//        public SystemMessageDto.SystemMessage makeSystemMessage(ModifiedChatInfo modifiedChatInfo, User actor){
//            if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.ENTER)){
//                return SystemMessageDto.EnterDto.builder()
//                        .type(ModifiedChatInfo.MemberStatus.ENTER)
//                        .roomId(modifiedChatInfo.getChatRoom().getRoomId())
//                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
//                        .nickname(modifiedChatInfo.getModifiedUser().getNickname())
//                        .imageUrl(modifiedChatInfo.getModifiedUser().getPictures().get(0).getStored_file_path())
//                        .build();
//            }else if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.EXIT)){
//                return SystemMessageDto.ExitDto.builder()
//                        .type(ModifiedChatInfo.MemberStatus.EXIT)
//                        .roomId(modifiedChatInfo.getChatRoom().getRoomId())
//                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
//                        .build();
//            }else if(modifiedChatInfo.getMemberStatus().equals(ModifiedChatInfo.MemberStatus.UPDATE)){
//                return SystemMessageDto.UpdateDto.builder()
//                        .type(ModifiedChatInfo.MemberStatus.UPDATE)
//                        .loginId(modifiedChatInfo.getModifiedUser().getLoginId())
//                        .nickname(modifiedChatInfo.getModifiedUser().getNickname())
//                        .imageUrl(modifiedChatInfo.getModifiedUser().getPictures().get(0).getStored_file_path())
//                        .build();
//            }else{
//                ChatRoom chatRoom = modifiedChatInfo.getChatRoom();
//                List<User> members = Arrays.asList(chatRoom.getUsers().get(0), chatRoom.getUsers().get(1));
//                List<String> chatRoomTitleAndImage = chatRoom.getChatRoomTitleAndImage(actor.getLoginId());
//                SystemMessageDto.RoomInfo roomInfo = new SystemMessageDto.RoomInfo(
//                        chatRoom.getRoomId(), chatRoom.getType(), chatRoomTitleAndImage.get(0), chatRoomTitleAndImage.get(1));
//                List<SystemMessageDto.MemberInfo> memberInfos = members.stream()
//                        .map(member -> new SystemMessageDto.MemberInfo(
//                                member.getLoginId(), member.getNickname(), member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());
//                return SystemMessageDto.MatchDto.builder()
//                        .type(ModifiedChatInfo.MemberStatus.MATCH)
//                        .roomInfo(roomInfo)
//                        .memberInfos(memberInfos)
//                        .build();
//            }
//
//        }
//    }
//
//    /* MATCH 때문에 변경되야할 수도. */
//    /** getChatMessageDto(앱 킬 때) 끝 */
//
//
//}