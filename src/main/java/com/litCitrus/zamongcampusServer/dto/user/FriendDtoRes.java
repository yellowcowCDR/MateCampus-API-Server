package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import com.litCitrus.zamongcampusServer.util.SecurityUtil;
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

        public ResWithDetail(User other, Friend friend){
            super(other, friend);
            this.collegeCode = other.getCollegeCode().name();
            this.majorCode = other.getMajor().getName();
            this.introduction = other.getIntroduction();
            // TODO: 1:N 관계라서 별도 Query 작성해야 함
            this.interests = other.getUserInterests().stream().map(InterestDtoRes::new).collect(Collectors.toList());
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
            this.requestorLoginId = SecurityUtil.getUser().getLoginId();
            this.status = friend.getStatus();
        }
    }
}
