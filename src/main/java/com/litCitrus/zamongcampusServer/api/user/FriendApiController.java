package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.dto.user.FriendDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.FriendDtoRes;
import com.litCitrus.zamongcampusServer.service.user.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/friend")
public class FriendApiController {

    private final FriendService friendService;

    // ** 친구신청
    @PostMapping()
    public ResponseEntity<?> requestFriend(@Valid @RequestBody FriendDtoReq.Create dto){
        friendService.requestFriend(dto);
        return new ResponseEntity<>("정상적인 접근: 팔로우 생성 ", HttpStatus.OK);
    }
    // ** 친구목록 불러오기
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<FriendDtoRes.Res> getFriends(){
        return friendService.getFriends();
    }

    @GetMapping("/approve")
    public ResponseEntity<List<FriendDtoRes.Res>> getApproveFriends(){
        return ResponseEntity.ok(friendService.getApproveFriends());
    }

    @GetMapping("{friendId}")
    @ResponseStatus(HttpStatus.OK)
    public FriendDtoRes.ResWithDetail getFriend(@Valid @PathVariable("friendId") String friendId){
        return friendService.getFriend(friendId);
    }
    // *** 친구수락, 거절도 followId 받아서도 진행가능할 듯. => 추후 변경 필요할지도.
    // ** 친구수락
    @PutMapping("/approve")
    public ResponseEntity<?> approveFriend(@Valid @RequestBody FriendDtoReq.Update dto){
        return new ResponseEntity<>("정상적인 접근: 팔로우 status 변경" + friendService.approveFriend(dto).getStatus(), HttpStatus.OK);
    }
    // ** 친구거절
    @PutMapping("/refuse")
    public ResponseEntity<?> refuseFriend(@Valid @RequestBody FriendDtoReq.Update dto){
        return new ResponseEntity<>("정상적인 접근: 팔로우 status 변경" + friendService.refuseFriend(dto).getStatus(), HttpStatus.OK);
    }
    // ** 친구삭제 => 양쪽 다 삭제할건지. 아니면 한쪽만 삭제할건지 고민. (이 부분이 가능할지 모르곘네)
    // 이미 반대쪽도 삭제한거면 삭제하도록. => (만약 한쪽에서 삭제한상태면 메세지가 가능하도록 해야하나.?)
    @DeleteMapping("{friendId}")
    public ResponseEntity<?> deleteFriend(@Valid @PathVariable("friendId") Long friendId){
        friendService.deleteFriend(friendId);
        return new ResponseEntity<>("정상적인 접근: 팔로우 삭제 ", HttpStatus.OK);
    }
}