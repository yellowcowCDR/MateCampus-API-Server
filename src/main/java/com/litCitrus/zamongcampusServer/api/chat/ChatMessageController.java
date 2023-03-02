package com.litCitrus.zamongcampusServer.api.chat;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatMessageDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.chat.ChatMessageService;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class ChatMessageController {
    Logger logger = LoggerFactory.getLogger(ChatMessageController.class);
    private final ChatMessageService chatMessageService;
    private final UserRepository userRepository;

    // ** client에서 /pub/chat/message로 메세지 전송하면, 오는 곳
    @MessageMapping("/chat/message")
    public void sendMessage(@Valid ChatMessageDtoReq messageDto, @Header("Authorization") String token) {
        logger.debug(messageDto.getLoginId());
        chatMessageService.sendMessage(messageDto, token);
    }

    @ResponseBody
    @GetMapping("/api/chat/message")
    public ChatMessageDtoRes.ChatBundle getChatMessageDynamo(@Valid @RequestParam("totalLastMsgCreatedAt") String createdAt){
        // 메시지 가져오는 부분
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByLoginId).orElseThrow(UserNotFoundException::new);
        return chatMessageService.getChatMessageDynamo(createdAt, user);
    }
}