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
            @Valid VoiceRoomDtoReq.Create dto) {
        return new ResponseEntity<>(voiceRoomService.createVoiceRoom(dto), HttpStatus.OK);
    }

//    @GetMapping
//    @ResponseStatus(HttpStatus.OK)
//    public List<PostDtoRes.Res> getVoiceRooms(@Valid @RequestParam("loginId") String loginId){
//        List<Post> posts = postService.getAllPostOrderbyRecent(loginId);
//        return posts.stream().map(post -> new PostDtoRes.Res(post)).collect(Collectors.toList());
//    }
//
    @GetMapping("{voiceRoomId}")
    public ResponseEntity<?> getVoiceRoom(@Valid @PathVariable("voiceRoomId") Long voiceRoomId, @RequestParam("loginId") String loginId){
        return new ResponseEntity<>(voiceRoomService.getVoiceRoom(voiceRoomId, loginId), HttpStatus.OK);
    }
//
//    // DELETE
//    @DeleteMapping("{voiceRoomId}")
//    public ResponseEntity<?> deletePost(@Valid @PathVariable("voiceRoomId") Long voiceRoomId, @RequestParam("loginId") String loginId){
//        postService.deletePost(voiceRoomId, loginId);
//        ResponseEntity<?> response = new ResponseEntity<>("정상적인 접근: 게시물 삭제", HttpStatus.OK);
//        return response;
//    }

}
