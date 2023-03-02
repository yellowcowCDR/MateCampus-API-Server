package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.interest.InterestCode;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoReq;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import com.litCitrus.zamongcampusServer.exception.user.UserNotFoundException;
import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserInterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserInterestService {

    private final UserInterestRepository userInterestRepository;
    private final InterestRepository interestRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<InterestDtoRes> updateMyInterests(List<InterestDtoReq> interestDtoReqList){
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User user = SecurityUtil.getCurrentUsername().flatMap(userRepository::findByLoginId).orElseThrow(UserNotFoundException::new);
        Set<UserInterest> newUserInterestList = new HashSet<>();
        for(InterestDtoReq interestDtoReq : interestDtoReqList){
            newUserInterestList.add(UserInterest.createUserInterest(user, interestRepository.findByInterestCode(InterestCode.valueOf(interestDtoReq.getInterestCode()))));
        }
        List<UserInterest> existUserInterests = userInterestRepository.findByUser(user);
        userInterestRepository.deleteAll(existUserInterests);
        List<UserInterest> userInterests = userInterestRepository.saveAll(newUserInterestList);
        return userInterests.stream().map(userInterest -> new InterestDtoRes(userInterest.getInterest())).collect(Collectors.toList());
    }
}
