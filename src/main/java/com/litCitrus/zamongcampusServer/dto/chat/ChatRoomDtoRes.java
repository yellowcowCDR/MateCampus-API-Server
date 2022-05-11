package com.litCitrus.zamongcampusServer.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ChatRoomDtoRes {

    // 이걸 쓰면 되긴 하는데, 로직이 좀.. 변경필요할지도. 
    final private SystemMessageDto.MatchDto matchDto;

}
