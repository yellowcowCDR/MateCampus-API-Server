package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.FriendDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.FriendDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.FriendRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final UserRepository userRepository;
    private final FriendRepository friendRepository;
    private final InterestRepository interestRepository;

    // ** 친구신청
    public void requestFriend(FriendDtoReq.Create dto){
        User actor = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(dto.getTargetLoginId()).orElseThrow(UserNotFoundException::new);
        friendRepository.save(Friend.createFriend(actor, target));
    }
    // ** 친구목록 불러오기
    public List<FriendDtoRes.Res> getFriends(){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        // TODO: querydsl 필요 (accepted 된 것들만 가져오는..?)
        return friendRepository.findByRequestorOrRecipient(user, user).stream()
                .map(friend -> new FriendDtoRes.Res(friend.getRequestor().equals(user) ? friend.getRecipient() : friend.getRequestor(), friend))
                .collect(Collectors.toList());
    }

    public FriendDtoRes.ResWithDetail getFriend(String friendId){
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Friend friend = friendRepository.findById(Long.parseLong(friendId)).get();
        User other = friend.getRequestor().equals(user) ? friend.getRecipient() : friend.getRequestor();
        List<Interest> otherInterests = interestRepository.findAllByUserInterests_User(other);
        return new FriendDtoRes.ResWithDetail(other, friend, otherInterests);
    }
    // ** 친구수락
    @Transactional
    public Friend approveFriend(FriendDtoReq.Update dto){
        User actor = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(dto.getTargetLoginId()).orElseThrow(UserNotFoundException::new);
        System.out.println(actor.getLoginId());
        System.out.println(target.getLoginId());
        return friendRepository.findByRequestorAndRecipient(target, actor).updateFriendStatus("accepted");
    }
    // ** 친구거절
    // *** 거절했을 때 삭제를 해버릴까. 아니면 거절하면 거절했던 유저입니다 라는 것이 떠야할까..? => 그게 아니라면 삭제 로직과 비슷
    @Transactional
    public Friend refuseFriend(FriendDtoReq.Update dto){
        User actor = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(dto.getTargetLoginId()).orElseThrow(UserNotFoundException::new);
        return friendRepository.findByRequestorAndRecipient(target, actor).updateFriendStatus("refused");
    }
    // ** 친구삭제
    @Transactional
    public void deleteFriend(Long friendId) {
        User actor = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        Friend friend = friendRepository.findById(friendId).get();
        // ** friend 객체 찾아서 requestor의 loginId를 삭제해주자.
        if (friend.getRecipient() == null || friend.getRequestor() == null) {
            // ** 상대방이 이미 삭제했다면 객체 삭제
            friendRepository.delete(friend);
        } else {
            if(friend.getRequestor().equals(actor)){
                friend.deleteRequestor();
            }else{
                friend.deleteRecipient();
            }
        }
    }
}
