package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.user.*;
import com.litCitrus.zamongcampusServer.dto.user.BlockedUserDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.BlockedUserNotFoundException;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.user.BlockedUserRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static com.litCitrus.zamongcampusServer.domain.user.BlockedUser.createBlockedUser;

@Transactional
@Service
public class BlockedUserService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BlockedUserRepository blockedUserRepository;

    public void addBlockedUser(String blockedUserLoginId){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);

        BlockedUser blockedUserEntity = createBlockedUser(requestedUser, blockedUser);

        blockedUserRepository.save(blockedUserEntity);
    }

    public List<BlockedUserDtoRes.Res> getBlockedUserList(){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        List<BlockedUser> blockedUserList =  blockedUserRepository.findByRequestedUser(requestedUser);

        List<BlockedUserDtoRes.Res> blockedUserResList = new ArrayList<>();

        for(BlockedUser blockedUser :blockedUserList){
            User blockedUserInfo = blockedUser.getBlockedUser();
            Long userId = blockedUser.getId ();
            String loginId = blockedUserInfo.getLoginId();
            String nickname = blockedUserInfo.getNickname();
            CollegeCode collegeCode = blockedUserInfo.getCollegeCode();
            String major = blockedUserInfo.getMajor().getName();
            List<UserPicture> userPictures  = blockedUserInfo.getPictures();
            String requestUserLoginId = requestedUser.getLoginId();
            BlockedUserDtoRes.Res blockedUserRes = new BlockedUserDtoRes.Res(userId, loginId, nickname, collegeCode, major, userPictures, requestUserLoginId);

            blockedUserResList.add(blockedUserRes);
        }

        return blockedUserResList;
    }

    /*public void getBlockedUser(String blockedUserLoginId){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);

        BlockedUser blockedUserEntity = blockedUserRepository.findByRequestedUserAndBlockedUser(requestedUser, blockedUser).orElseThrow(UserNotFoundException::new);

        User receivedRequestedUser = blockedUserEntity.getRequestedUser();
        User receivedBlockedUser = blockedUserEntity.getBlockedUser();

    }*/

    public Boolean isBlockedUser(String blockedUserLoginId){
        User requestedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);

        Boolean isBlockedUser=blockedUserRepository.existsByRequestedUserAndBlockedUser(requestedUser, blockedUser);
        return isBlockedUser;
    }

    public void deleteBlockedUser(String blockedUserLoginId){
        User requestedUser = SecurityUtil.getCurrentUsername().flatMap(userRepository::findOneWithAuthoritiesByLoginId).orElseThrow(UserNotFoundException::new);
        User blockedUser = userRepository.findByLoginId(blockedUserLoginId).orElseThrow(UserNotFoundException::new);

        BlockedUser blockedUserInfo= blockedUserRepository.findByRequestedUserAndBlockedUser(requestedUser, blockedUser).orElseThrow(BlockedUserNotFoundException::new);

        blockedUserRepository.delete(blockedUserInfo);
    }


}
