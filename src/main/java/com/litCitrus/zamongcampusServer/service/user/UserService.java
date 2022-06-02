package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.chat.Participant;
import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.interest.InterestCode;
import com.litCitrus.zamongcampusServer.domain.post.PostPicture;
import com.litCitrus.zamongcampusServer.domain.user.*;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.UserDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
import com.litCitrus.zamongcampusServer.repository.post.PostRepository;
import com.litCitrus.zamongcampusServer.repository.user.FriendRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserInterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserPictureRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.service.chat.SystemMessageComponent;
import com.litCitrus.zamongcampusServer.service.image.S3Uploader;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParticipantRepository participantRepository;
    private final SystemMessageComponent systemMessageComponent;
    private final UserPictureRepository userPictureRepository;
    private final UserInterestRepository userInterestRepository;
    private final InterestRepository interestRepository;
    private final FriendRepository friendRepository;
    private final PostRepository postRepository;
    private final S3Uploader s3Uploader;


    /** 회원가입
     * 자동으로 ROLE_USER의 권한을 가진다.
     * ROLE_ADMIN는 data.sql에서 서버 실행과 함께 하나만 생성한다. (따로 메소드로 두진 않는다 위험해서)
     * */
    @Transactional
    public User signup(UserDtoReq.Create userDto) throws IOException {
        if (userRepository.findOneWithAuthoritiesByLoginId(userDto.getLoginId()).orElse(null) != null) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다.");
        }
        System.out.println(userDto.getInterestCodes());
        Authority authority = Authority.builder()
                .authorityName("ROLE_USER")
                .build();
        User user = User.createUser(userDto, passwordEncoder.encode(userDto.getPassword()), authority);
        userRepository.save(user);
        if(userDto.getStudentIdImg() != null){
            String uploadImageUrl = s3Uploader.uploadOne(userDto.getStudentIdImg(), "2022/userIdImage");
            user.setStudentIdImageUrl(uploadImageUrl);
        }
        if(userDto.getProfileImg() != null){
            String uploadImageUrl = s3Uploader.uploadOne(userDto.getProfileImg(), "2022/user");
            UserPicture userPicture = UserPicture.createUserPicture(user, uploadImageUrl);
            userPictureRepository.save(userPicture);
            user.addPicture(userPicture); // 이거 필요 없어 보이는데??
        }
        if(!userDto.getInterestCodes().isEmpty()){
            List<UserInterest> userInterests = new ArrayList<>();
            for(String interestCode : userDto.getInterestCodes()) {
                Interest interest = interestRepository.findByInterestCode(InterestCode.valueOf(interestCode));
                userInterestRepository.save(UserInterest.createUserInterest(user, interest));
            }
        }
        return user;
    }

    @Transactional
    public void updateDeviceToken(UserDtoReq.Update userDto){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        user.updateDeviceToken(userDto.getDeviceToken());
    }

    /* 관리자(ADMIN)만 사용 */
    @Transactional(readOnly = true)
    public Optional<User> getUserWithAuthorities(String loginId) {
        return userRepository.findOneWithAuthoritiesByLoginId(loginId);
    }

    /* 일반 User들이 사용 */
    @Transactional(readOnly = true)
    public UserDtoRes.ResForMyPage getMyUserInfoInMyPage() {
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        long friendsCount = friendRepository.findByRequestorOrRecipient(user, user).stream().filter(friend -> friend.getStatus() == Friend.Status.ACCEPTED).count();
        long postsCount = user.getPosts().size();
        long commentsCount = user.getComments().size();
        return new UserDtoRes.ResForMyPage(user, friendsCount, postsCount, commentsCount);
    }

    public List<UserDtoRes.ResForRecommend> getRecommendUsers(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        // 나중에 querydsl로 변경해야함.
        // 1. accepted인 친구만
        // 2. friend에서 나 말고 다른 사람
        // 3. 본인 제외 모든 유저 불러옴.
        User admin = userRepository.findByLoginId("admin").get();
        List<String> friendLoginIds = friendRepository.findByRequestorOrRecipient(user, user).stream()
                .filter(friend -> friend.getStatus() == Friend.Status.ACCEPTED)
                .map(friend -> friend.getRequestor().getLoginId() == user.getLoginId() ? friend.getRecipient().getLoginId() : friend.getRequestor().getLoginId())
                .collect(Collectors.toList());
        friendLoginIds.add("admin"); // admin 아이디 추가
        List<User> allUsersExceptFriend = userRepository.findAllByLoginIdIsNotContaining(user.getLoginId()).stream()
                .filter(u -> !friendLoginIds.contains(u.getLoginId())).collect(Collectors.toList());
        return allUsersExceptFriend.stream().map(UserDtoRes.ResForRecommend::new).collect(Collectors.toList());
    }

    @Transactional
    public void updateUserInfo(UserDtoReq.Update userUpdateDto) {

        /* 1. 변경 정보를 MySQL에 저장 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        /// TODO: user image 내용
//        if(userUpdateDto.getProfileImage() != null){
//            List<String> uploadImageUrl = s3Uploader.upload(Arrays.asList(userProfileDtoReq.getProfileImage()), "2021/user");
//            UserPicture userProfilePicture = uploadImageUrl.stream().map(url -> UserPicture.createUserPicture(modifiedUser, url)).collect(Collectors.toList()).get(0);
//            userPictureRepository.save(userProfilePicture);
//
//            user.updatePicture(userProfilePicture);
//        }
        if(userUpdateDto.getNickname() != null){
            user.updateUserNickname(userUpdateDto.getNickname());
        }
        /* 해당 유저와 같은 방에 있는 멤버 찾기 */
        List<Participant> participants = participantRepository.findByUsers_loginId(user.getLoginId());
        Set<User> recipients =
                participants.stream()
                        .flatMap(participant -> participant.getUsers().stream())
                        .collect(Collectors.toSet());
        System.out.println(recipients.size());
        /* 멤버들에게 실시간으로 정보제공 */
        systemMessageComponent.sendSaveUpdateSystemMessage(user, recipients);
    }

    @Transactional
    public User activateUser(String loginId){
        User user = userRepository.findOneWithAuthoritiesByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        user.setActivated();
        return user;
    }
}
