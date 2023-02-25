package com.litCitrus.zamongcampusServer.dto.chat;

import com.litCitrus.zamongcampusServer.domain.chat.ChatRoom;
import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ChatRoomDtoRes {

    final private SystemMessageDto.CreateDto createDto;

    public ChatRoomDtoRes(ChatRoom chatRoom, List<User> members, User actor) {
        List<String> chatRoomTitleAndImage = chatRoom.getCounterpartChatRoomTitleAndImage(actor.getLoginId());
        SystemMessageDto.RoomInfo roomInfo = new SystemMessageDto.RoomInfo(
                chatRoom.getRoomId(), chatRoom.getType(), chatRoomTitleAndImage.get(0), chatRoomTitleAndImage.get(1));

        /* 채팅방을 요청한 사용자 */
        SystemMessageDto.MemberInfo requestedMember = new SystemMessageDto.MemberInfo(actor);
        String actorId = actor.getLoginId();

        /* 상대 참여자 목록 */
        List<SystemMessageDto.MemberInfo> otherMembers = members.stream()
                .filter(member-> !member.getLoginId().equals(actorId))
                .map(member -> new SystemMessageDto.MemberInfo(member.getId(),
                        member.getLoginId(), member.getNickname(), member.getPictures().isEmpty() ? null : member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());

        /* 모든 참여자 목록 */
        List<SystemMessageDto.MemberInfo> memberInfos = members.stream()
                .map(member -> new SystemMessageDto.MemberInfo(member.getId(),
                        member.getLoginId(), member.getNickname(), member.getPictures().isEmpty() ? null : member.getPictures().get(0).getStored_file_path())).collect(Collectors.toList());

        this.createDto = SystemMessageDto.CreateDto.builder()
                .type(ModifiedChatInfo.MemberStatus.CREATE)
                .roomInfo(roomInfo)
                .requestedMember(requestedMember)
                .otherMembers(otherMembers)
                .memberInfos(memberInfos)
                .build();
    }

    public ChatRoomDtoRes(){
        createDto = null;
    }
}
