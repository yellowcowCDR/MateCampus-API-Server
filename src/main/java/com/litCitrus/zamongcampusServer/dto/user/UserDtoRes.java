package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;
import lombok.Getter;

public class UserDtoRes {
    /// interest까지 포함해서 나중에 전달.
    @Getter
    public static class ResForMyPage {
        private final String loginId;
        private final String nickname;
        private final String collegeCode;
        private final String majorCode;
        private final String imageUrl;
        private final long interestCount;
        private final long friendCount;
        private final long bookMarkCount;
        private final long myPostCount;
        private final long myCommentCount;

        public ResForMyPage(User me, long friendCount, long postCount, long commentCount){
            this.loginId = me.getLoginId();
            this.nickname = me.getNickname();
            this.collegeCode = me.getCollegeCode().name();
            this.majorCode = me.getMajorCode().name();
            this.imageUrl = me.getPictures().isEmpty() ? "empty" : me.getPictures().get(0).getStored_file_path();
            this.interestCount = me.getUserInterests().stream().count();
            this.friendCount = friendCount;
            this.bookMarkCount = 10;
            this.myPostCount = postCount;
            this.myCommentCount = commentCount;
        }
    }

    @Getter
    public static class ResForRecommend{
        private final String loginId;
        private final String imageUrl;
        private final String collegeCode;
        private final String majorName;
        private final boolean isOnline;

        public ResForRecommend(User user){
            this.loginId = user.getLoginId();
            this.imageUrl = user.getPictures().isEmpty() ? "" : user.getPictures().get(0).getStored_file_path();
            this.collegeCode = user.getCollegeCode().name();
            this.majorName = user.getMajorCode().name();
            this.isOnline = true;
        }


    }
}


