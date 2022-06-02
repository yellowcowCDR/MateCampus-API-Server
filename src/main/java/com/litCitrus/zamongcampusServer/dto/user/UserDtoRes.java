package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.user.User;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

public class UserDtoRes {
    /// interest까지 포함해서 나중에 전달.
    @Getter
    public static class ResForMyPage extends CommonRes{
        private final long interestCount;
        private final long friendCount;
        private final long bookMarkCount;
        private final long myPostCount;
        private final long myCommentCount;

        public ResForMyPage(User me, long friendCount, long postCount, long commentCount){
            super(me);
            this.interestCount = me.getUserInterests().stream().count();
            this.friendCount = friendCount;
            this.bookMarkCount = 10;
            this.myPostCount = postCount;
            this.myCommentCount = commentCount;
        }
    }

    @Getter
    public static class CommonRes{
        private final String loginId;
        private final String nickname;
        private final String imageUrl;
        private final String collegeCode;
        private final String majorCode;
        private final boolean isOnline;

        public CommonRes(User user){
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
            this.imageUrl = user.getPictures().isEmpty() ? "" : user.getPictures().get(0).getStored_file_path();
            this.collegeCode = user.getCollegeCode().name();
            this.majorCode = user.getMajorCode().name();
            this.isOnline = true;
        }
    }

    @Getter
    public static class ResForDetailInfo extends CommonRes{

        private final List<InterestDtoRes> interests;
        public ResForDetailInfo(User user, List<Interest> interests){
            super(user);
            this.interests = interests.stream().map(InterestDtoRes::new).collect(Collectors.toList());

        }
    }
}


