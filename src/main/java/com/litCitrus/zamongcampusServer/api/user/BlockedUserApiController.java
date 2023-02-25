package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.BlockedUserDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.service.user.BlockedUserService;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/blockedUser")
@RestController
@RequiredArgsConstructor
public class BlockedUserApiController {

    final private UserRepository userRepository;

    final private BlockedUserService blockedUserService;

    @PostMapping
    public ResponseEntity addBlockedUser(String blockedUserLoginId){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);

        if(blockedUserService.isBlockedUser(requestedUser, blockedUser)){
            return new ResponseEntity<>("user blocked already.", HttpStatus.ACCEPTED);
        }else{
            blockedUserService.addBlockedUser(blockedUserLoginId);
            return new ResponseEntity<>("user blocked.", HttpStatus.CREATED);
        }
    }

    @GetMapping("/list")
    public ResponseEntity<List<BlockedUserDtoRes.Res>> getBlockedUserList(){
        List<BlockedUserDtoRes.Res> blockedUserList = blockedUserService.getBlockedUserList();
        return ResponseEntity.ok(blockedUserList);
    }

    @GetMapping("/checkIfBlocked/{blockedUserLoginId}")
    public ResponseEntity<Boolean> checkIfBlocked(@PathVariable String blockedUserLoginId){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);

        Boolean isBlockedUser = blockedUserService.isBlockedUser(requestedUser, blockedUser);
        return ResponseEntity.ok(isBlockedUser);
    }
    @DeleteMapping("/{blockedUserLoginId}")
    public HttpStatus deleteBlockedUser(@PathVariable String blockedUserLoginId){
        blockedUserService.deleteBlockedUser(blockedUserLoginId);
        return HttpStatus.OK;
    }



}
