package com.litCitrus.zamongcampusServer.api.chat;

import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoRes;
import com.litCitrus.zamongcampusServer.service.chat.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    final private ChatRoomService chatRoomService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ChatRoomDtoRes createOrGetChatRoom(@Valid @RequestBody ChatRoomDtoReq.Create chatRoomDto){
        return chatRoomService.createOrGetChatRoom(chatRoomDto);
    }

    @PutMapping("{chatRoomId}/enter")
    @ResponseStatus(HttpStatus.OK)
    void enterChatRoom(@Valid @PathVariable("chatRoomId") Long chatRoomId){
        chatRoomService.enterChatRoom(chatRoomId);
    }

    @PutMapping("{chatRoomId}/exit")
    @ResponseStatus(HttpStatus.OK)
    void exitChatRoom(@Valid @PathVariable("chatRoomId") Long chatRoomId){
        chatRoomService.exitChatRoom(chatRoomId);
    }

}
