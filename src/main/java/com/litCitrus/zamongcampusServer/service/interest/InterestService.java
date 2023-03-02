package com.litCitrus.zamongcampusServer.service.interest;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import com.litCitrus.zamongcampusServer.repository.interest.InterestRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InterestService {

    private final InterestRepository interestRepository;
    private final UserRepository userRepository;

    public List<InterestDtoRes> getMyInterests(){
        //ToDo 로그인된 유저 정보 가져오는 방법 수정
        User user = SecurityUtil.getUser();
        List<Interest> interests = interestRepository.findAllByUserInterests_User(user);
        return interests.stream().map(InterestDtoRes::new).collect(Collectors.toList());
    }
}
