package com.litCitrus.zamongcampusServer.dto.user;

import com.litCitrus.zamongcampusServer.domain.user.Friend;
import com.litCitrus.zamongcampusServer.domain.user.User;

public class FriendDtoRes {
    private Long id;
    private String loginId;
    private String imageUrl;
    private String nickname;

    public FriendDtoRes(User other, Friend friend){
        this.id = friend.getId();
        this.loginId = other.getLoginId();
        this.imageUrl = other.getPictures().get(0).getStored_file_path();
        this.nickname = other.getNickname();
    }
}
