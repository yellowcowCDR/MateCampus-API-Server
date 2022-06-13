package com.litCitrus.zamongcampusServer.service.user;

import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.domain.user.UserPicture;
import com.litCitrus.zamongcampusServer.repository.user.UserPictureRepository;
import com.litCitrus.zamongcampusServer.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 이건 과거 코드인듯 삭제할 수도. (06.13)
@Slf4j
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final UserRepository userRepository;
    private final UserPictureRepository userPictureRepository;

    public void signup(User user) {
        checkDuplicate(user);
        userRepository.save(user);
        List<UserPicture> pictures1 = new ArrayList<UserPicture>(Arrays.asList(UserPicture.createUserPicture(user, "https://atti-test-orangebooksorg-images.s3.ap-northeast-2.amazonaws.com/2021/user/20211104/user3.jpg")));
        userPictureRepository.saveAll(pictures1);
        userRepository.save(user);
        // TODO: signup 하고 바로 저장한다고 ? 최종 완료가 되어야 저장하는거 아니고? email까지 가고.
        // 아니다. signup하고 일단 user 만들어야 중간 중복 안 생김
        // 그러고 email 인증 안된 유저와 인증된 유저로 isValidate으로 분기처리 하면 될듯
        // MAIL_CERTIFIED
        // isMailCertified
    }

    private void checkDuplicate(User user) {
        if (userRepository.existsUserByLoginId(user.getLoginId())){
            log.info("Here1");
//			throw new BusinessException(ErrorCode.INVALID_INPUT_VALUE);
//			throw new HttpClientErrorException.BadRequest("");
        }

        if (userRepository.existsUserByNickname(user.getNickname())){
            log.info("Here3");
            throw new IllegalStateException("이미 존재하는 닉네임입니다");
        }
    }

}
