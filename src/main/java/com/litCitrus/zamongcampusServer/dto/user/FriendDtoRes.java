package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class FriendDtoRes {

    @Getter
    public static class ResWithDetail extends Res{
        final private String collegeCode;
        final private String majorCode;
        final private String introduction;
        private final List<InterestDtoRes> interests;

        public ResWithDetail(User other, Friend friend, List<Interest> interests){
            super(other, friend);
            this.collegeCode = other.getCollegeCode().name();
            this.majorCode = other.getMajorCode().name();
            this.introduction = other.getIntroduction();
            this.interests = interests.stream().map(InterestDtoRes::new).collect(Collectors.toList());
        }
    }

    @Getter
    public static class Res {
        private Long id;
        private String loginId;
        private String imageUrl;
        private String nickname;
        private String requestorLoginId;
        private Friend.Status status;

        public Res(User other, Friend friend) {
            this.id = friend.getId();
            this.loginId = other.getLoginId();
            this.imageUrl = other.getPictures().isEmpty() ? null : other.getPictures().get(0).getStored_file_path();
            this.nickname = other.getNickname();
            this.requestorLoginId = friend.getRequestor().getLoginId();
            this.status = friend.getStatus();
        }
    }
}
