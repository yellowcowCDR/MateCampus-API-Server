package com.litCitrus.zamongcampusServer.api.voiceRoom;

import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoReq;
import com.litCitrus.zamongcampusServer.dto.post.PostDtoRes;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoReq;
import com.litCitrus.zamongcampusServer.dto.voiceRoom.VoiceRoomDtoRes;
import com.litCitrus.zamongcampusServer.service.voiceRoom.VoiceRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/voiceRoom")
public class VoiceRoomApiController {

    private final VoiceRoomService voiceRoomService;

    @PostMapping
    public ResponseEntity<?> createVoiceRoom(
            @Valid @RequestBody VoiceRoomDtoReq.Create dto) {
        return new ResponseEntity<>(voiceRoomService.createVoiceRoom(dto), HttpStatus.CREATED);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<VoiceRoomDtoRes.Res> getVoiceRooms(){
        return voiceRoomService.getVoiceRooms();
    }

    @GetMapping("{voiceRoomId}")
    public ResponseEntity<?> joinAndGetVoiceRoom(@Valid @PathVariable("voiceRoomId") Long voiceRoomId){
        return new ResponseEntity<>(voiceRoomService.joinAndGetVoiceRoom(voiceRoomId), HttpStatus.OK);
    }

    @PutMapping({"/exit/{voiceRoomId}"})
    public ResponseEntity<?> exitVoiceRoom(@Valid @PathVariable("voiceRoomId") Long voiceRoomId){
        voiceRoomService.exitVoiceRoom(voiceRoomId);
        return new ResponseEntity<>("정상접근: 방 나가기 완료", HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("{voiceRoomId}/invite")
    public void inviteMembers(@Valid @PathVariable("voiceRoomId") Long voiceRoomId, @Valid VoiceRoomDtoReq.UpdateInvite dto){
        voiceRoomService.inviteMembers(voiceRoomId, dto);
    }
}
