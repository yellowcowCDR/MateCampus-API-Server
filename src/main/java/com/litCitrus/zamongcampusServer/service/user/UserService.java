package com.litCitrus.zamongcampusServer.service.user;

import com.google.common.collect.Iterables;
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
import com.litCitrus.zamongcampusServer.repository.user.*;
import com.litCitrus.zamongcampusServer.repository.voiceRoom.ParticipantRepository;
import com.litCitrus.zamongcampusServer.service.chat.SystemMessageComponent;
import com.litCitrus.zamongcampusServer.service.image.S3Uploader;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import com.litCitrus.zamongcampusServer.util.UserComparatorForSort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RecommendUserRepository recommendUserRepository;
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

    @Transactional
    public List<UserDtoRes.ResWithMajorCollege> getRecommendUsers(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        if(!user.getRecommendUsers().stream().filter(recommendUser -> recommendUser.isActivated()).findAny().isPresent()){
            // 처음 회원가입 후 로그인한 경우
            return makeNewRecommendUsersAndReturn(true, user);
        }else{
            // 새벽 12시~ 오후 12시면 그 전날의 date(ex.6.16 정오)를, 오후 12시(정오)가 되면 그 날의 date(ex. 6.17 정오)를 줘야한다.
            LocalDateTime twelveNoon = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 12, 0);
            LocalDateTime midnight = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0);
            if(LocalDateTime.now().isBefore(twelveNoon) && LocalDateTime.now().isAfter(midnight)){
                twelveNoon = twelveNoon.minusDays(1);
            }
            // 새벽 12시부터 오후 12시 사이에 하게 되면 아래 코드가 항상 false가 된다.
            if(Iterables.getLast(user.getRecommendUsers()).getUpdatedAt().isAfter(twelveNoon)){
                // 기존 친구 반환
                return user.getRecommendUsers().stream()
                        .filter(recommendUser -> recommendUser.isActivated())
                        .map(recommendUser -> recommendUser.getRecommendedUser())
                        .sorted(new UserComparatorForSort())
                        .map(sortedUser -> new UserDtoRes.ResWithMajorCollege(sortedUser)).collect(Collectors.toList());
            }else{
                // 기존 친구 삭제 후 새로 지정
                return makeNewRecommendUsersAndReturn(false, user);
            }
        }
    }

    @Transactional
    public List<UserDtoRes.ResWithMajorCollege> makeNewRecommendUsersAndReturn(boolean isNew, User user){
        if(!isNew){
            for(RecommendUser recommendUser : user.getRecommendUsers()){
                recommendUser.updateActivated(false);
            }
        }
        // 제외할 사람 외 나머지 유저들 중 랜덤으로 5명 선정
        // 제외할 사람: 1. accepted, refused, UNACCEPTED 된 친구 2. admin
        // 단, 실제로 1번 제약조건은 그냥 friend 다 불러오면 된다. 모든 친구가 위 3개의 status 중 하나이기 때문. (NONE는 불가능)
        List<String> friendLoginIds = friendRepository.findByRequestorOrRecipient(user, user).stream()
                .map(friend -> friend.getRequestor().getLoginId() == user.getLoginId() ? friend.getRecipient().getLoginId() : friend.getRequestor().getLoginId())
                .collect(Collectors.toList());
        friendLoginIds.add("admin"); // admin 아이디 추가
        List<User> allUsersExceptFriend = userRepository.findAllByLoginIdIsNot(user.getLoginId())
                .stream()
                .filter(u -> !friendLoginIds.contains(u.getLoginId()))
                .filter(u -> u.isActivated()).collect(Collectors.toList());
        Collections.shuffle(allUsersExceptFriend);
        int randomRecommendUsersLength = 5;
        List<User> randomRecommendUsers = allUsersExceptFriend.subList(0, allUsersExceptFriend.size() < 5 ? allUsersExceptFriend.size() : randomRecommendUsersLength);
        for(User randomUser : randomRecommendUsers){
            if(recommendUserRepository.existsByRecipientAndRecommendedUser(user, randomUser)){
                RecommendUser recommendUser = recommendUserRepository.findByRecipientAndRecommendedUser(user, randomUser);
                recommendUser.plusCount();
                recommendUser.updateActivated(true);
            } else{
                recommendUserRepository.save(RecommendUser.createRecommendUser(user, randomUser));
            }
        }
        randomRecommendUsers.sort(new UserComparatorForSort());
        return randomRecommendUsers.stream().map(UserDtoRes.ResWithMajorCollege::new).collect(Collectors.toList());
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
    public UserDtoRes.CommonRes updateUserInfo(UserDtoReq.Update userUpdateDto) throws IOException {

        /* 1. 변경 정보를 MySQL에 저장 */
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        String imageUrl = user.getPictures().isEmpty() ? "" : user.getPictures().get(0).getStored_file_path();
        /// TODO: user image 내용
        if(userUpdateDto.getProfileImage() != null){
            /* 1-1. profileImage: 기존 사진 url 삭제 후, 재 삽입. */
            userPictureRepository.deleteAll(user.getPictures());
            List<String> uploadImageUrl = s3Uploader.upload(Arrays.asList(userUpdateDto.getProfileImage()), "2022/user");
            imageUrl = uploadImageUrl.get(0);
            List<UserPicture> userProfilePictures = uploadImageUrl.stream().map(url -> UserPicture.createUserPicture(user, url)).collect(Collectors.toList());
            userPictureRepository.saveAll(userProfilePictures);
        }
        if(userUpdateDto.getNickname() != null){
            /* 1-2. nickname */
            user.updateNickname(userUpdateDto.getNickname());
        }
        if(userUpdateDto.getIntroduction() != null){
            /* 1-3. introduction */
            user.updateIntroduction(userUpdateDto.getIntroduction());
        }

        /* 2. 변경 정보를 같은 채팅방 친구들에게 보낸다. (nickname, imageUrl이 변경된 경우에) */
        /* 2-1. 유저와 같은 방에 있는 멤버 찾기 */
        if(userUpdateDto.getProfileImage() != null || userUpdateDto.getNickname() != null){
            List<Participant> participants = participantRepository.findByUsers_loginId(user.getLoginId());
            Set<User> recipients =
                    participants.stream()
                            .flatMap(participant -> participant.getUsers().stream())
                            .collect(Collectors.toSet());
            /* 2-2. 멤버들에게 실시간 전송 및 ModifiedInfo에 저장 */
            systemMessageComponent.sendSaveUpdateSystemMessage(user, recipients, imageUrl);
        }

        return new UserDtoRes.CommonRes(user, imageUrl);
    }

    @Transactional
    public User activateUser(String loginId){
        User user = userRepository.findOneWithAuthoritiesByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        user.updateActivated(true);
        return user;
    }
}
