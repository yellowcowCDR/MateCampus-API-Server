package com.litCitrus.zamongcampusServer.api.chat;

import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoReq;
import com.litCitrus.zamongcampusServer.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    final private ChatRoomService chatRoomService;

    @PostMapping
    ResponseEntity<?> createChatRoom(@Valid @RequestBody ChatRoomDtoReq chatRoomDto){
        chatRoomService.createChatRoom(chatRoomDto);
    }
}
