//package com.litCitrus.zamongcampusServer.dto.user;
//
//import com.litCitrus.zamongcampusServer.domain.user.Friend;
//import com.litCitrus.zamongcampusServer.domain.user.User;
//import lombok.Getter;
//
//@Getter
//public class LoginDtoRes {
//    // 혹시 나중에 필요할 수도 있으나, 현 시점에서는 필요 없어 보임.
//    // 필요한게 token, loginId, nickname, interestsCode,
//    // token, loginId만 주자.
//    // 마이페이지 누를 때 nickname, interestsCode 등 줄 것.
//    // 처음부터 너무 많은 Load를 하면 안된다.
//
//    final private String token;
//    final private String log;
//    final private String introduction;
//
//    public ResWithDetail(User other, Friend friend){
//        super(other, friend);
//        this.collegeCode = other.getCollegeCode();
//        this.majorCode = other.getMajorCode();
//        this.introduction = other.getIntroduction();
//    }
//}
