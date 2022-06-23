package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @ModelAttribute UserDtoReq.Create userDto) throws IOException {
        User user = userService.signup(userDto);
        return new ResponseEntity<>("정상적인 접근: 회원가입 완료", HttpStatus.CREATED);
    }

    @PostMapping("/user/updateDeviceToken")
    public void updateDeviceToken(@Valid @RequestBody UserDtoReq.Update userDto){
        userService.updateDeviceToken(userDto);
    }

    @GetMapping("/user/recommend")
    public ResponseEntity<List<UserDtoRes.ResWithMajorCollege>> getRecommendUsers(){
        return ResponseEntity.ok(userService.getRecommendUsers());
    }

    @GetMapping("/user/recentTalkAndFriend")
    public ResponseEntity<UserDtoRes.ResForRecentTalkFriend> getRecentTalkAndFriendUsers(@Valid @RequestParam("recentTalkUserLoginIds") List<String> recentTalkUserLoginIds){
        return ResponseEntity.ok(userService.getRecentTalkAndFriendUsers(recentTalkUserLoginIds));
    }

    @GetMapping("/user/mypage")
    @PreAuthorize("hasAnyRole('USER','ADMIN')") // <- 이거 나중에 보고 써보려고 나둔 것. 사실 따로 필요없음
    public ResponseEntity<UserDtoRes.ResForMyPage> getMyUserInfoInMyPage() {
        return ResponseEntity.ok(userService.getMyUserInfoInMyPage());
    }

    @GetMapping("/user/info/{loginId}")
    public ResponseEntity<UserDtoRes.ResForDetailInfo> getOtherUserInfo(@Valid @PathVariable String loginId){
        return ResponseEntity.ok(userService.getOtherUserInfo(loginId));
    }

    @PutMapping("/user")
    public ResponseEntity<?> updateUserInfo(@Valid @ModelAttribute UserDtoReq.Update dto) throws IOException {
        return ResponseEntity.ok(userService.updateUserInfo(dto));
    }

    /** 어드민만 가능한 함수
     * PreAuthorize로 어드민만 접근 가능하도록 함
     * */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfoByAdmin(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }

    @PostMapping("/user/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> activateUser(@RequestParam("loginId") String loginId) {
        User user = userService.activateUser(loginId);
        return ResponseEntity.ok("정상접근: " + user.getLoginId() + " 활성화 완료");
    }
}
