package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.user.FriendDtoReq;
import com.litCitrus.zamongcampusServer.dto.user.FriendDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.FriendRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
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

    // ** 친구신청
    public void requestFriend(String targetLoginId, FriendDtoReq.Create dto){
        User actor = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(targetLoginId).orElseThrow(UserNotFoundException::new);
        friendRepository.save(Friend.createFriend(actor, target));
    }
    // ** 친구목록 불러오기
    public List<FriendDtoRes> getFriends(String loginId){
        User user = userRepository.findByLoginId(loginId).orElseThrow(UserNotFoundException::new);
        return friendRepository.findByRequestorOrRecipient(user, user).stream()
                .map(friend -> new FriendDtoRes(friend.getRequestor().equals(user) ? friend.getRecipient() : friend.getRequestor(), friend))
                .collect(Collectors.toList());
    }
    // ** 친구수락
    @Transactional
    public Friend approveFriend(String targetLoginId, FriendDtoReq.Update dto){
        User actor = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(targetLoginId).orElseThrow(UserNotFoundException::new);
        return friendRepository.findByRequestorAndRecipient(target, actor).updateFriendStatus("approved");
    }
    // ** 친구거절
    // *** 거절했을 때 삭제를 해버릴까. 아니면 거절하면 거절했던 유저입니다 라는 것이 떠야할까..? => 그게 아니라면 삭제 로직과 비슷
    @Transactional
    public Friend refuseFriend(String targetLoginId, FriendDtoReq.Update dto){
        User actor = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(UserNotFoundException::new);
        User target = userRepository.findByLoginId(targetLoginId).orElseThrow(UserNotFoundException::new);
        return friendRepository.findByRequestorAndRecipient(target, actor).updateFriendStatus("refused");
    }
    // ** 친구삭제
    @Transactional
    public void deleteFriend(Long friendId, FriendDtoReq.Update dto) {
        User actor = userRepository.findByLoginId(dto.getLoginId()).orElseThrow(UserNotFoundException::new);
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
