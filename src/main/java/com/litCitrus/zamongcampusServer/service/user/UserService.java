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

    public List<UserDtoRes.ResWithMajorCollege> getRecommendUsers(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        // 나중에 querydsl로 변경해야함.
        /// 제외시킬 사람 유형
        // 1. none인 friend 뺴고 다 (accepted, request중, 거절된 사람 포함)
        // 내가 신청자면 request 중인 것도 가져오지만, 내가 신청자가 아니면 가지오지 않도록.
        // 1. A: 신청자, B: 받는이
        // 1-1. A에게는 신청중인 것도 나오고 , B는 신청중인 것이 안나와야한다.
        // 1-2. 따라서 상대가 신청중이고 신청자가 자신인 경우는 A의 경우고. A는 이런 경우가 본인한테 나타나야하기에 값이 false여야한다.
        // 1-3. 반대로 B는 신청중인 상태이지만 자신의 로그인 아이디가 아니라서 false이고, !부정때문에 true로 되어 friendLoginIds에 해당 건이 추가된다.
        // 1-4. 즉, B에게는 이 경우가 나타나지 않는다.
        // 2. 그 friend에서 나 말고 다른 사람
        // 3. 본인 제외 모든 유저 불러옴.
        /** 이 부분은 다시 고려해서 만들 것.
        // 왜냐하면 추천친구가 예를들어 24시간 지나면 바뀌거나 그런식으로 할 것이기 때문에
        // 로직이 아예 달라질 수도 있다.
        */
        List<String> friendLoginIds = friendRepository.findByRequestorOrRecipient(user, user).stream()
//                .filter(friend -> !friend.getStatus().equals(Friend.Status.NONE))
                // 이 아래 식이 잘 안 먹힌다.
//                .filter(friend -> !(friend.getStatus().equals(Friend.Status.UNACCEPTED) && friend.getRequestor().getLoginId().equals(user.getLoginId())))
                .filter(friend -> friend.getStatus().equals(Friend.Status.ACCEPTED) || friend.getStatus().equals(Friend.Status.REFUSED))

                .map(friend -> friend.getRequestor().getLoginId() == user.getLoginId() ? friend.getRecipient().getLoginId() : friend.getRequestor().getLoginId())
                .collect(Collectors.toList());
        friendLoginIds.add("admin"); // admin 아이디 추가
        List<User> allUsersExceptFriend = userRepository.findAllByLoginIdIsNotContaining(user.getLoginId()).stream()
                .filter(u -> !friendLoginIds.contains(u.getLoginId())).collect(Collectors.toList());
        return allUsersExceptFriend.stream().map(UserDtoRes.ResWithMajorCollege::new).collect(Collectors.toList());
    }

    public UserDtoRes.ResForRecentTalkFriend getRecentTalkAndFriendUsers(List<String> recentTalkUserLoginIds){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<User> recentTalkUsers = userRepository.findAllByLoginIdIsIn(recentTalkUserLoginIds);
        List<User> approveFriends = friendRepository.findByRequestorOrRecipient(user, user).stream()
                .filter(friend -> friend.getStatus().equals(Friend.Status.ACCEPTED))
                .map(friend -> friend.getRequestor().equals(user) ? friend.getRecipient() : friend.getRequestor())
                .collect(Collectors.toList());

        return new UserDtoRes.ResForRecentTalkFriend(recentTalkUsers, approveFriends);
    }

    public UserDtoRes.ResForDetailInfo getOtherUserInfo(String loginId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User other = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        List<Interest> interests = interestRepository.findAllByUserInterests_User(other);
        Friend friendWhichUserIsRequestor = friendRepository.findByRequestorAndRecipient(user, other);
        Friend friendWhichUserIsRecipient = friendRepository.findByRequestorAndRecipient(other, user);
        if(friendWhichUserIsRequestor != null || friendWhichUserIsRecipient != null){
            return new UserDtoRes.ResForDetailInfo(other, interests, Friend.Status.UNACCEPTED);
        }
        return new UserDtoRes.ResForDetailInfo(other, interests, Friend.Status.NONE);

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
