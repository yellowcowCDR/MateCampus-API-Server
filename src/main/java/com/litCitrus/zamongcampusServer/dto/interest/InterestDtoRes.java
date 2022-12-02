package com.litCitrus.zamongcampusServer.dto.interest;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.UserInterest;
import lombok.Getter;

@Getter
public class InterestDtoRes {

    private final String interestCode;

    public InterestDtoRes(Interest interest){
        this.interestCode = interest.getInterestCode().name();
    }

    public InterestDtoRes(UserInterest userInterest){
        this(userInterest.getInterest());
    }
}
