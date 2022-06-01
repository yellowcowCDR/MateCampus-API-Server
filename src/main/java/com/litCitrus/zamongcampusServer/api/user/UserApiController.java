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

    @GetMapping("/user/mypage")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<UserDtoRes.ResForMyPage> getMyUserInfoInMyPage() {
        return ResponseEntity.ok(userService.getMyUserInfoInMyPage());
    }

    /** 어드민만 가능한 함수
     * PreAuthorize로 어드민만 접근 가능하도록 함
     * */
    @GetMapping("/user/{username}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        return ResponseEntity.ok(userService.getUserWithAuthorities(username).get());
    }

    @PostMapping("/user/activate")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> activateUser(@RequestParam("loginId") String loginId) {
        User user = userService.activateUser(loginId);
        return ResponseEntity.ok("정상접근: " + user.getLoginId() + " 활성화 완료");
    }
}
