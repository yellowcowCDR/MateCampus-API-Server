package com.litCitrus.zamongcampusServer.dto.chat;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.litCitrus.zamongcampusServer.domain.user.ModifiedChatInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * SystemMessage DTO
 * - type column의 중복을 없애기 위해 JsonSubTypes, JsonTypeInfo로 SuperBuilder 활용
 * - 의미: 일반메세지(enter,exit,talk)과 시스템메시지(enter,exit,update,create)이 섞인 존재
 * - 역할:
 */
@Getter
public class SystemMessageDto {

    @SuperBuilder
    @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
    @JsonSubTypes({
            @JsonSubTypes.Type(value = EnterDto.class, name = "enter"),
            @JsonSubTypes.Type(value = ExitDto.class, name = "exit"),
            @JsonSubTypes.Type(value = UpdateDto.class, name = "update"),
            @JsonSubTypes.Type(value = CreateDto.class, name = "create"),
            @JsonSubTypes.Type(value = ChatMessageDtoRes.RealTimeMessageBundle.class, name = "talk")
    })
    public static class SystemMessage{
        private ModifiedChatInfo.MemberStatus type;
    }

    // ** createdAt = 실시간을 위해서 필요
    @Getter
    @SuperBuilder
    public static class EnterDto extends SystemMessage{
        private String roomId;
        private String loginId;
        private String nickname;
        private String imageUrl;
        private String createdAt;
        private String body;
    }

    @Getter
    @SuperBuilder
    public static class ExitDto extends SystemMessage{
        private String roomId;
        private String loginId;
        private String nickname;
        private String createdAt;
        private String body;
    }
    @Getter
    @SuperBuilder
    public static class UpdateDto extends SystemMessage{
        private String loginId;
        private String nickname;
        private String imageUrl;
    }

    @Getter
    @SuperBuilder
    public static class CreateDto extends SystemMessage{
        final private RoomInfo roomInfo;
        final private List<MemberInfo> memberInfos;
    }

    @Getter
    @AllArgsConstructor
    public static class RoomInfo{
        private String roomId;
        private String type;
        private String title;
        private String imageUrl;

    }

    @Getter
    @AllArgsConstructor
    public static class MemberInfo{
        private Long id;
        private String loginId;
        private String nickname;
        private String imageUrl;
    }
}

