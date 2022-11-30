package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.dto.user.BlockedUserDtoRes;
import com.litCitrus.zamongcampusServer.service.user.BlockedUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/blockedUser")
@RestController
public class BlockedUserApiController {

    @Autowired
    BlockedUserService blockedUserService;

    @PostMapping
    public ResponseEntity addBlockedUser(String blockedUserLoginId){
        blockedUserService.addBlockedUser(blockedUserLoginId);
        return new ResponseEntity<>("user blocked.", HttpStatus.CREATED);
    }

    @GetMapping("/list")
    public ResponseEntity<List<BlockedUserDtoRes.Res>> getBlockedUserList(){
        List<BlockedUserDtoRes.Res> blockedUserList = blockedUserService.getBlockedUserList();
        return ResponseEntity.ok(blockedUserList);
    }

    @GetMapping("/checkIfBlocked/{blockedUserLoginId}")
    public ResponseEntity<Boolean> checkIfBlocked(@PathVariable String blockedUserLoginId){
        Boolean isBlockedUser = blockedUserService.isBlockedUser(blockedUserLoginId);
        return ResponseEntity.ok(isBlockedUser);
    }
    @DeleteMapping("/{blockedUserLoginId}")
    public HttpStatus deleteBlockedUser(@PathVariable String blockedUserLoginId){
        blockedUserService.deleteBlockedUser(blockedUserLoginId);
        return HttpStatus.OK;
    }



}
