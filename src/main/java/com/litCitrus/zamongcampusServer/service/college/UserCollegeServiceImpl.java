package com.litCitrus.zamongcampusServer.service.college;

import com.litCitrus.zamongcampusServer.domain.user.UserCollege;
import com.litCitrus.zamongcampusServer.exception.college.UserCollegeException;
import com.litCitrus.zamongcampusServer.repository.college.UserCollegeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class UserCollegeServiceImpl implements UserCollegeService{
    private final UserCollegeRepository userCollegeRepository;

    @Override
    public UserCollege getUserCollege(Long Id) {
        return userCollegeRepository.findById(Id).orElseThrow(()-> UserCollegeException.NOT_FOUND);
    }

    @Override
    public void save(UserCollege userCollege) {
        userCollegeRepository.save(userCollege);
    }
}
