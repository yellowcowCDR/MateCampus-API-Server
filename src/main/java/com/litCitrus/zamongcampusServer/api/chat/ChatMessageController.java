package com.litCitrus.zamongcampusServer.api.chat;

import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoRes;
import com.litCitrus.zamongcampusServer.service.chat.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {

    private final ChatMessageService chatMessageService;

    // ** client에서 /pub/chat/message로 메세지 전송하면, 오는 곳
    @MessageMapping("/chat/message")
    public void sendMessage(@Valid ChatMessageDtoReq messageDto) {
        chatMessageService.sendMessage(messageDto);
    }

    @ResponseBody
    @GetMapping("/api/chat/message")
    public ChatMessageDtoRes.ChatBundle getChatMessageDynamo(@Valid @RequestParam("totalLastMsgCreatedAt") String createdAt){
        // 메시지 가져오는 부분
        return chatMessageService.getChatMessageDynamo(createdAt);
    }

}