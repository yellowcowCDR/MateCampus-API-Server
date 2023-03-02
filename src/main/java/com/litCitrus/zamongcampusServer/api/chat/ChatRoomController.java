package com.litCitrus.zamongcampusServer.api.chat;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.chat.ChatRoomDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.chat.ChatRoomService;
import com.litCitrus.zamongcampusServer.service.user.BlockedUserService;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/chat/room")
@RequiredArgsConstructor
public class ChatRoomController {

    final private UserRepository userRepository;

    final private ChatRoomService chatRoomService;

    final private BlockedUserService blockedUserService;

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    // ChatRoomDtoRes createOrGetChatRoom(@Valid @RequestBody ChatRoomDtoReq.Create chatRoomDto){
    ResponseEntity<ChatRoomDtoRes> createOrGetChatRoom(@Valid @RequestBody ChatRoomDtoReq.Create chatRoomDto){
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User requestedUser = SecurityUtil.getUser();
        User otherLoginUser = userRepository.findByLoginId(chatRoomDto.getOtherLoginId()).orElseThrow(UserNotFoundException::new);
        if(blockedUserService.isBlockedUser(otherLoginUser.getLoginId(), requestedUser.getLoginId())){
            return new ResponseEntity<ChatRoomDtoRes>(new ChatRoomDtoRes(), HttpStatus.FORBIDDEN);
        }else{
            ChatRoomDtoRes ChatRoomInfo = chatRoomService.createOrGetChatRoom(chatRoomDto);
            return new ResponseEntity<ChatRoomDtoRes>(ChatRoomInfo, HttpStatus.CREATED);
        }
    }

    /// 단톡방 만드는 api 반드시 따로 만들 것
    /// particpant의 hashcode가 겹치면 안되기 때문에.
    /// 단톡방을 불러오는 getOneDetailMultiRoom도 따로 만들어야할 듯. -> 이 함수는 또 필요는 없음.
    /// 단톡방은 무조건 roomId를 넘기기에 roomId로 찾으면 되어서 문제 없을 듯.
    /// 1:1 방은 무조건 loginId로 된 hashcode로 방을 찾기에 반드시 다르게 생각할 것.

    @PutMapping("{chatRoomId}/enter")
    @ResponseStatus(HttpStatus.OK)
    void enterChatRoom(@Valid @PathVariable("chatRoomId") Long chatRoomId){
        chatRoomService.enterChatRoom(chatRoomId);
    }

    @PutMapping("{chatRoomId}/exit")
    @ResponseStatus(HttpStatus.OK)
    void exitChatRoom(@Valid @PathVariable("chatRoomId") String roomId){
        chatRoomService.exitChatRoom(roomId);
    }

}
