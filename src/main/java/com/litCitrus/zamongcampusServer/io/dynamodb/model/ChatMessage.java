package com.litCitrus.zamongcampusServer.io.dynamodb.model;

import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import lombok.*;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ChatMessage {
    private String roomId;
    private String createdAt;
    private String type;
    private String text;
    private String loginId;

    // getters
    @DynamoDbPartitionKey
    public String getRoomId(){
        return this.roomId;
    }

    @DynamoDbSortKey
    public String getCreatedAt(){
        return this.createdAt;
    }

    public static ChatMessage createChatMessage(ChatMessageDtoReq dtoReq, String currentTime){
        // loginId가 없어서 오류가 있을 수도 있음.
        return ChatMessage.builder()
                .roomId(dtoReq.getRoomId())
                .createdAt(currentTime)
                .type(dtoReq.getType())
                .text(dtoReq.getText())
                .loginId(dtoReq.getLoginId())
                .build();
    }

}