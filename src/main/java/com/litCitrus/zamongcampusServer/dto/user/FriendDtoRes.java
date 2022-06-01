package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;

public class FriendDtoRes {


    /// interest까지 포함해서 나중에 전달.
    @Getter
    public static class ResWithDetail extends Res{
        final private String collegeCode;
        final private String majorCode;
        final private String introduction;

        public ResWithDetail(User other, Friend friend){
            super(other, friend);
            this.collegeCode = other.getCollegeCode().name();
            this.majorCode = other.getMajorCode().name();
            this.introduction = other.getIntroduction();
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
            this.imageUrl = other.getPictures().size() != 0 ? other.getPictures().get(0).getStored_file_path() : null;
            this.nickname = other.getNickname();
            this.requestorLoginId = friend.getRequestor().getLoginId();
            this.status = friend.getStatus();
        }
    }
}
