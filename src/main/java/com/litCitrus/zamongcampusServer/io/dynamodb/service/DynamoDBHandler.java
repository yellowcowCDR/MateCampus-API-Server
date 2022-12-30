package com.litCitrus.zamongcampusServer.io.dynamodb.service;

import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import com.litCitrus.zamongcampusServer.io.dynamodb.model.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

@RequiredArgsConstructor
@Component
public class DynamoDBHandler {

    private final DynamoDbEnhancedClient enhancedClient;

    /** 메세지 넣기 */
    public boolean putMessage(ChatMessageDtoReq dtoReq, String senderLoginId, String currentTime){
        try {
            // 1. 테이블 세팅
            DynamoDbTable<ChatMessage> chatMessageTable = enhancedClient.table("ChatMessage", TableSchema.fromBean(ChatMessage.class));
            // 2. 데이터 세팅
            ChatMessage chatMessage = ChatMessage.createChatMessage(dtoReq, currentTime, senderLoginId);
            // 3. 데이터 넣기
            chatMessageTable.putItem(chatMessage);
            return true;
        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            return false;
        }
    }
    // loginId 없는 시스템메시지지만, 위의 함수로 동일하게 사용 가능할 수도 있다.
//    public boolean putMessage(String roomId, String createdAt, String type, String text){
//        try {
//            // 테이블 세팅
//            DynamoDbTable<ChatMessage> chatMessageTable = enhancedClient.table("ChatMessage", TableSchema.fromBean(ChatMessage.class));
//
//            // DB에 넣을 데이터 세팅
//            ChatMessage chatMessage = new ChatMessage();
//            chatMessage.setCreatedAt(createdAt);
//            chatMessage.setRoomId(roomId);
//            chatMessage.setType(type);
//            chatMessage.setText(text);
//            // DB에 데이터 넣기
//            chatMessageTable.putItem(chatMessage);
//            return true;
//        } catch (DynamoDbException e) {
//            System.err.println(e.getMessage());
//            return false;
//        }
//    }
    /** 메시지 가져오기 */
    public PageIterable<ChatMessage> getMessages(String roomId, String createdAfter){
        try {
            // 테이블 세팅
            DynamoDbTable<ChatMessage> chatMessageTable = enhancedClient.table("ChatMessage", TableSchema.fromBean(ChatMessage.class));

            // 검색할 프라이머리키 생성
            Key startKey = Key.builder()
                    .partitionValue(roomId)
                    .sortValue(createdAfter)
                    .build();
            QueryConditional queryConditional = QueryConditional.sortGreaterThan(startKey);

            // 키로 아이템 가져오기
            return chatMessageTable.query(queryConditional);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            return null;
        }

    }

    public PageIterable<ChatMessage> getMessages(String roomId){
        try {
            // 테이블 세팅
            DynamoDbTable<ChatMessage> chatMessageTable = enhancedClient.table("ChatMessage", TableSchema.fromBean(ChatMessage.class));

            // 검색할 프라이머리키 생성
            Key startKey = Key.builder()
                    .partitionValue(roomId)
                    .build();
            QueryConditional queryConditional = QueryConditional.sortGreaterThan(startKey);

            // 키로 아이템 가져오기
            return chatMessageTable.query(queryConditional);

        } catch (DynamoDbException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
