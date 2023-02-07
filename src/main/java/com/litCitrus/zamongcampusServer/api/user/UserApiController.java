package com.litCitrus.zamongcampusServer.api.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.service.college.CampusService;
import com.litCitrus.zamongcampusServer.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    private final CampusService campusService;

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

    @GetMapping("/signup/id/duplication/{id}")
    public ResponseEntity<Boolean> checkIdDuplication(@PathVariable String id){
        return ResponseEntity.ok(userService.checkIdDuplication(id));
    }

    @GetMapping("/signup/nickname/duplication/{nickname}")
    public ResponseEntity<Boolean> checkNicknameDuplication(@PathVariable String nickname){
        return ResponseEntity.ok(userService.checkNicknameDuplication(nickname));
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

    @GetMapping("/users")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<UserDtoRes.ResForCheckMember>> getUsers() {
        return ResponseEntity.ok(userService.findAll().stream()
                .map(u -> new UserDtoRes.ResForCheckMember(u.getId()
                        , u.getLoginId()
                        , u.getNickname()
                        , u.getCampus().getCollege().getCollegeName()
                        , u.getMajor().getName()
                        , u.isActivated()
                        , u.getStudentIdImageUrl()))
                .collect(Collectors.toList()));
    }

    @PatchMapping("/user/activate/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> updateUserActivation(@PathVariable("id") Long userId, boolean activated) {
        User user = userService.activateUser(userId, activated);
        return ResponseEntity.ok("정상접근: " + user.getLoginId() + " 활성화 상태 "+ activated +"로 설정 완료");
    }
}
