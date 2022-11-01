package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.interest.Interest;
import com.litCitrus.zamongcampusServer.domain.notification.Notification;
import com.litCitrus.zamongcampusServer.domain.post.Post;
import com.litCitrus.zamongcampusServer.domain.post.PostBookMark;
import com.litCitrus.zamongcampusServer.domain.post.PostComment;
import com.litCitrus.zamongcampusServer.domain.post.PostLike;
import com.litCitrus.zamongcampusServer.domain.user.*;
import com.litCitrus.zamongcampusServer.domain.voiceRoom.VoiceRoom;
import com.litCitrus.zamongcampusServer.dto.interest.InterestDtoRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDtoRes {
    @Getter
    public static class ResForMyPage extends ResWithMajorCollege{
        private final long interestCount;
        private final long friendCount;
        private final long bookMarkCount;
        private final long myPostCount;
        private final long myCommentCount;

        public ResForMyPage(User me, long friendCount, long bookMarkCount, long postCount, long commentCount){
            super(me);
            this.interestCount = me.getUserInterests().stream().count();
            this.friendCount = friendCount;
            this.bookMarkCount = bookMarkCount;
            this.myPostCount = postCount;
            this.myCommentCount = commentCount;
        }
    }

    @Getter
    @SuperBuilder
    public static class CommonRes{
        private final String loginId;
        private final String nickname;
        private final String imageUrl;
        private final boolean isOnline; // stomp에 연결한 사람으로 알 수 있지 않을까?
        private final String introduction;

        public CommonRes(User user){
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
            this.imageUrl = user.getPictures().isEmpty() ? null : user.getPictures().get(0).getStored_file_path();
            this.isOnline = true;
            this.introduction = user.getIntroduction();
        }

        public CommonRes(User user, String imageUrl){
            this.loginId = user.getLoginId();
            this.nickname = user.getNickname();
            this.imageUrl = imageUrl;
            this.isOnline = true;
            this.introduction = user.getIntroduction();
        }
    }

    @Getter
    public static class ResWithMajorCollege extends CommonRes{
        private final String collegeCode;
        private final String majorCode;
        public ResWithMajorCollege(User user){
            super(user);
            this.collegeCode = user.getCollegeCode().name();
            this.majorCode = user.getMajorCode().name();
        }
    }

    @Getter
    public static class ResForRecentTalkFriend{
        private final List<CommonRes> recentTalkUsers;
        private final List<CommonRes> approveFriends;

        public ResForRecentTalkFriend(List<User> recentTalkUsers, List<User> approveFriends){
            this.recentTalkUsers = recentTalkUsers.stream().map(CommonRes::new).collect(Collectors.toList());
            this.approveFriends = approveFriends.stream().map(CommonRes::new).collect(Collectors.toList());
        }
    }

    @Getter
    public static class ResForDetailInfo extends ResWithMajorCollege{

        private final List<InterestDtoRes> interests;
        private Friend.Status friendStatus;
        public ResForDetailInfo(User other, List<Interest> interests, Friend.Status friendStatus){
            super(other);
            this.friendStatus = friendStatus;
            this.interests = interests.stream().map(InterestDtoRes::new).collect(Collectors.toList());

        }
    }

    @Getter
    public static class ResForPostLikedUsers{
        private String loginId;

        private String nickname;

        private CollegeCode collegeCode;


        private MajorCode majorCode;

        private List<String> imageUrls;

        public ResForPostLikedUsers(String loginId, String nickname, CollegeCode collegeCode, MajorCode majorCode, List<UserPicture> pictures){
            this.loginId = loginId;
            this.nickname = nickname;
            this.collegeCode = collegeCode;
            this.majorCode = majorCode;
            this.imageUrls = new ArrayList<String>();
            for(UserPicture picture : pictures){
                this.imageUrls.add(picture.getStored_file_path());
            }
        }
    }


}


